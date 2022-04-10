package deep.auth.service

import deep.auth.dto.UserRegistryDTO
import deep.auth.model.User
import deep.auth.model.UserAuthDetails
import deep.auth.repository.UserAuthDetailsRepository
import deep.auth.repository.UserRepository
import org.springframework.stereotype.Service

interface RegistrationService {
    fun registration(userDTO: UserRegistryDTO)
}

@Service
class RegistrationServiceImpl (
    val userAuthDetailsRepository: UserAuthDetailsRepository,
    val userRepository: UserRepository
    ) : RegistrationService {
    override fun registration(userDTO: UserRegistryDTO) {
        val user = User(
            userDTO.firstName,
            userDTO.lastName,
            userDTO.middleName
        )

        val userDetails = UserAuthDetails(
            userDTO.password
        )
        userDetails.codeCreating(user.id)

        userRepository.save(user)
        userAuthDetailsRepository.save(userDetails)
    }
}