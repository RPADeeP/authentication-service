package deep.auth.service

import deep.auth.repository.UserRepository
import org.springframework.stereotype.Service

interface UserService {
    fun addCompanyTokenToUser(token: String, code: String)
}

@Service
class UserServiceImpl(
    val userRepository: UserRepository
) : UserService {
    override fun addCompanyTokenToUser(token: String, code: String) {
        if (code.isEmpty())
            return

        val user = userRepository.findByCode(code)
        user.companyToken = token
        userRepository.save(user)
    }

}