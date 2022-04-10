package deep.auth.service

import deep.auth.dto.UserLoginDTO
import deep.auth.repository.UserAuthDetailsRepository
import org.springframework.stereotype.Service

interface LoginService {
    fun login(userLoginDTO: UserLoginDTO) : Boolean
}

@Service
class LoginServiceImpl(
    val userRepository: UserAuthDetailsRepository
) : LoginService {
    override fun login(userLoginDTO: UserLoginDTO) : Boolean {
        return userRepository.findByCodeAndPassword(userLoginDTO.code, userLoginDTO.password) != null
    }
}