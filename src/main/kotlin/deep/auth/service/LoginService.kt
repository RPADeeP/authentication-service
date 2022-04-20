package deep.auth.service

import deep.auth.dto.UserLoginDTO
import deep.auth.repository.UserAuthDetailsRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

interface LoginService {
    fun login(userLoginDTO: UserLoginDTO) : String
}

@Service
class LoginServiceImpl(
    val userRepository: UserAuthDetailsRepository,
    val tokenService: TokenService
) : LoginService {
    override fun login(userLoginDTO: UserLoginDTO) : String {
        return try {
            tokenService.generateToken(userRepository.findByCodeAndPassword(userLoginDTO.code, userLoginDTO.password))
        } catch (e: EmptyResultDataAccessException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found")
        }
    }
}