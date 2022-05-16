package deep.auth.repository

import deep.auth.model.Role
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : MongoRepository<Role, Int>{
    fun findByCompanyToken(companyToken: String) : List<Role>?
    fun findDefaultRoleByNameAndCompanyToken(name: String, companyToken: String) : Role
}