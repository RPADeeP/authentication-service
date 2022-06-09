package deep.auth.repository

import deep.auth.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, Int> {
    fun findById(id: String) : User?
    fun findByIdAndCode(id: String, code: String) : User?
    fun findAllByCompanyTokenAndDepartmentIsNull(companyToken: String) : List<User>?
    fun findAllByCompanyToken(companyToken: String) : List<User>?
    fun findAllByIdIn(id: List<String>) : ArrayList<User>?
    fun findAllByDepartment_Id(department_id: String) : ArrayList<User>?
}