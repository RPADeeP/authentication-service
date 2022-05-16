package deep.auth.service

import deep.auth.model.User
import deep.auth.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

interface UserService {
    fun addCompanyTokenToUser(token: String, userToken: User)
    fun getAllUsersForCompany(companyToken: String) : List<User>
    fun getUser(userId: ObjectId) : User?
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    override fun addCompanyTokenToUser(token: String, userToken: User) {
        if (userToken.code.isEmpty())
            return

        val user = userRepository.findByIdAndCode(userToken.id , userToken.code) ?: return

        user.companyToken = token
        userRepository.save(user)
    }

    override fun getAllUsersForCompany(companyToken: String) : List<User> {
        return userRepository.findAllByCompanyToken(companyToken) ?: emptyList()
    }

    override fun getUser(userId: ObjectId): User? {
        return userRepository.findById(userId)
    }

}