package deep.auth.repository

import deep.auth.model.UserAuthDetails
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserAuthDetailsRepository : MongoRepository<UserAuthDetails, Int> {
    fun findByCodeAndPassword(code: String, password: String) : UserAuthDetails?
    fun findById(id: String) : UserAuthDetails?
}