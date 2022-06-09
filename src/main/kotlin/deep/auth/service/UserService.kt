package deep.auth.service

import deep.auth.dto.*
import deep.auth.model.Department
import deep.auth.model.User
import deep.auth.repository.UserRepository
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.server.ResponseStatusException

interface UserService {
    fun addCompanyTokenToUser(token: String, userToken: User)
    fun getAllUsersNoDepartment(companyToken: String) : List<User>
    fun getAllUsers(companyToken: String) : List<User>
    fun getUser(userId: String) : User?
    fun addRoleToUser(addRoleToUserDTO: AddRoleToUserDTO)
    fun addUserToDepartment(addUserToDepartment: AddUserToDepartmentDTO, token: String)
    fun deleteUserFromDepartment(deleteUserFromDepartmentDTO: DeleteUserFromDepartmentDTO, token: String)
    fun deleteDepartmentFromUser(deleteDepartmentDTO: DeleteDepartmentDTO): Boolean
    fun changeUserDepartmentName(changeNameDepartmentDTO: ChangeNameDepartmentDTO): Boolean
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val restTemplate: RestTemplate,
    private val departmentServiceUrl: String
) : UserService {
    override fun addCompanyTokenToUser(token: String, userToken: User) {
        if (userToken.code.isEmpty())
            return

        val user = userRepository.findByIdAndCode(userToken.id , userToken.code) ?: return

        user.companyToken = token
        userRepository.save(user)
    }

    override fun getAllUsersNoDepartment(companyToken: String) : List<User> = userRepository.findAllByCompanyTokenAndDepartmentIsNull(companyToken) ?: emptyList()

    override fun getAllUsers(companyToken: String): List<User> = userRepository.findAllByCompanyToken(companyToken) ?: emptyList()

    override fun getUser(userId: String): User? = userRepository.findById(userId)

    override fun addRoleToUser(addRoleToUserDTO: AddRoleToUserDTO) {
        userRepository.findById(addRoleToUserDTO.userId).also {
            it ?: throw NotFoundException()
            it.role = addRoleToUserDTO.assignRole
            userRepository.save(it)
        }
    }

    override fun addUserToDepartment(addUserToDepartment: AddUserToDepartmentDTO, token: String) {
        val user = userRepository.findById(addUserToDepartment.userId) ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found")
        user.department = Department(
            addUserToDepartment.departmentId,
            addUserToDepartment.name
        )
        addUserToDepartment.users = listOf(user)
        addUserToDepartment.companyToken

        val header = HttpHeaders()
        header.set("Authorization", "Bearer $token");
        val requestEntity: HttpEntity<AddUserToDepartmentDTO> = HttpEntity(addUserToDepartment, header)

        try {
            val entity = restTemplate.exchange("$departmentServiceUrl/department/add-users", HttpMethod.POST, requestEntity, String::class.java)
            if(entity.statusCodeValue == 200)
                userRepository.save(user)
            else
                throw ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.stackTrace.toString())
        }
     }

    override fun deleteUserFromDepartment(deleteUserFromDepartmentDTO: DeleteUserFromDepartmentDTO, token: String) {
        val user = userRepository.findById(deleteUserFromDepartmentDTO.userId) ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found")
        deleteUserFromDepartmentDTO.departmentId = user.department?.id
        user.department = null

        val header = HttpHeaders()
        header.set("Authorization", "Bearer $token");
        val requestEntity: HttpEntity<DeleteUserFromDepartmentDTO> = HttpEntity(deleteUserFromDepartmentDTO, header)

        try {
            val entity = restTemplate.exchange("$departmentServiceUrl/department/delete-user", HttpMethod.POST, requestEntity, String::class.java)
            if(entity.body.toBoolean())
                userRepository.save(user)
            else
                throw ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.stackTrace.toString())
        }
    }

    override fun deleteDepartmentFromUser(deleteDepartmentDTO: DeleteDepartmentDTO): Boolean {
        val users = userRepository.findAllByIdIn(deleteDepartmentDTO.usersIds) ?: return false
        users.map {
            user ->
            user.department = null
        }
        userRepository.saveAll(users)
        return true
    }

    override fun changeUserDepartmentName(changeNameDepartmentDTO: ChangeNameDepartmentDTO): Boolean {
        val users = userRepository.findAllByDepartment_Id(changeNameDepartmentDTO.departmentId) ?: return false
        try {
            users.forEach { user ->
                user.department?.name = changeNameDepartmentDTO.newName
            }
        } catch (e: Exception) {
            return false
        }
        userRepository.saveAll(users)
        return true
    }
}