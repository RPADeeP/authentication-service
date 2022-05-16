package deep.auth.repository

import deep.auth.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, Int> {
    fun findById(id: ObjectId) : User?
    fun findByIdAndCode(id: ObjectId, code: String) : User?
    fun findAllByCompanyToken(companyToken: String) : List<User>?
    fun findByCode(code: String) : User
}