package deep.auth.service

import deep.auth.dto.UserRegistryDTO
import deep.auth.model.User
import deep.auth.repository.UserRepository
import org.springframework.stereotype.Service

interface RegistrationService {
    fun registration(userDTO: UserRegistryDTO)
}

@Service
class RegistrationServiceImpl (
    val userRepository: UserRepository
    ) : RegistrationService {
    override fun registration(userDTO: UserRegistryDTO) {
        val user = User(
            userDTO.firstName,
            userDTO.lastName,
            userDTO.middleName,
            userDTO.password
        )
        user.code = "${user.id}".replace("[a-zA-Z]".toRegex(), "")
        userRepository.save(user)
    }
}