package deep.auth.service

import deep.auth.dto.CompanyCreateDTO
import deep.auth.dto.TokenCallBack
import deep.auth.dto.UserRegistryDTO
import deep.auth.model.User
import deep.auth.model.UserAuthDetails
import deep.auth.repository.RoleRepository
import deep.auth.repository.UserAuthDetailsRepository
import deep.auth.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.server.ResponseStatusException

interface RegistrationService {
    fun registration(userDTO: UserRegistryDTO): TokenCallBack
}

@Service
class RegistrationServiceImpl (
    private val userAuthDetailsRepository: UserAuthDetailsRepository,
    private val tokenService: TokenService,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val restTemplate: RestTemplate
    ) : RegistrationService {

    @Value("\${app.company-create.url}")
    private lateinit var companyCreateUrl: String

    override fun registration(userDTO: UserRegistryDTO): TokenCallBack {
        val user = User(
            userDTO.firstName,
            userDTO.lastName,
            userDTO.middleName,
            userDTO.companyToken
        )

        val userDetails = UserAuthDetails(
            userDTO.password
        )

        user.code = userDetails.code

        userAuthDetailsRepository.save(userDetails)

        val token = tokenService.generateToken(userDetails, user)

        if(userDTO.companyToken.isBlank()) {
            val headers = HttpHeaders()
            headers.set("Authorization", "Bearer $token");
            val requestEntity: HttpEntity<CompanyCreateDTO> = HttpEntity(CompanyCreateDTO(userDTO.companyName), headers)

            try {
                val entity = restTemplate.exchange("$companyCreateUrl/company/create", HttpMethod.POST, requestEntity, String::class.java)
                if(entity.statusCodeValue == 200)
                    user.companyToken = entity.body.toString()
                else
                    throw ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE)
            } catch (e: Exception) {
                userAuthDetailsRepository.delete(userDetails)
                throw ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.stackTrace.toString())
            }

            user.role = roleRepository.findDefaultRoleByNameAndCompanyToken("admin", "")

        } else
            user.role = roleRepository.findDefaultRoleByNameAndCompanyToken("default", "")

        userRepository.save(user)

        return TokenCallBack(
            token,
            user.companyToken
        )
    }
}