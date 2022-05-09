package deep.auth.service

import deep.auth.model.User
import deep.auth.repository.UserRepository
import org.springframework.stereotype.Service

interface UserService {
    fun addCompanyTokenToUser(token: String, userToken: User)
    fun getAllUsersForCompany(companyToken: String) : List<User>
}

@Service
class UserServiceImpl(
    val userRepository: UserRepository
) : UserService {
    override fun addCompanyTokenToUser(token: String, userToken: User) {
        if (userToken.code.isEmpty())
            return

        val user = userRepository.findByIdAndCode(userToken.id, userToken.code) ?: return

        user.companyToken = token
        userRepository.save(user)
    }

    override fun getAllUsersForCompany(companyToken: String) : List<User> {
        return userRepository.findAllByCompanyToken(companyToken) ?: emptyList()
    }

}