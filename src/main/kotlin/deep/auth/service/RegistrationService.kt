package deep.auth.service

import deep.auth.dto.TokenCallBack
import deep.auth.dto.UserRegistryDTO
import deep.auth.model.User
import deep.auth.model.UserAuthDetails
import deep.auth.repository.UserAuthDetailsRepository
import deep.auth.repository.UserRepository
import org.springframework.stereotype.Service

interface RegistrationService {
    fun registration(userDTO: UserRegistryDTO): TokenCallBack
}

@Service
class RegistrationServiceImpl (
    val userAuthDetailsRepository: UserAuthDetailsRepository,
    val tokenService: TokenService,
    val userRepository: UserRepository
    ) : RegistrationService {
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

        userRepository.save(user)
        userAuthDetailsRepository.save(userDetails)
        return TokenCallBack(
            tokenService.generateToken(userDetails, user),
            user.companyToken
        )
    }
}