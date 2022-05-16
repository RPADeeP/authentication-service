package deep.auth.service

import deep.auth.dto.TokenCallBack
import deep.auth.dto.UserLoginDTO
import deep.auth.model.User
import deep.auth.model.UserAuthDetails
import deep.auth.repository.UserAuthDetailsRepository
import deep.auth.repository.UserRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

interface LoginService {
    fun login(userLoginDTO: UserLoginDTO) : TokenCallBack
}

@Service
class LoginServiceImpl(
    private val userAuthDetailsRepository: UserAuthDetailsRepository,
    private val userRepository: UserRepository,
    private val tokenService: TokenService
) : LoginService {
    override fun login(userLoginDTO: UserLoginDTO) : TokenCallBack {
        val userAuthDetails : UserAuthDetails?
        val user : User?
        try {
            userAuthDetails = userAuthDetailsRepository.findByCodeAndPassword(userLoginDTO.code, userLoginDTO.password)
            user = userRepository.findByCode(userLoginDTO.code)
        } catch (e: EmptyResultDataAccessException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found")
        }
        return TokenCallBack(
            tokenService.generateToken(userAuthDetails, user),
            user.companyToken
        )
    }
}