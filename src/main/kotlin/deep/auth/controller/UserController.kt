package deep.auth.controller

import deep.auth.dto.*
import deep.auth.model.User
import deep.auth.service.TokenService
import deep.auth.service.UserService
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/user")
class UserController(
    val userService: UserService,
    val tokenService: TokenService
) {

    @GetMapping(path = ["/add-token/{token}"])
    fun addCompanyTokenToUser(@PathVariable token: String, request: HttpServletRequest)
    = userService.addCompanyTokenToUser(token,
            tokenService.parseToken(tokenService.getTokenFromAuth(request.getHeader(HttpHeaders.AUTHORIZATION)), "User") as User)

    @GetMapping(path = ["/get-current-user"])
    fun getCurrentUser(request: HttpServletRequest) : User
    = tokenService.parseToken(tokenService.getTokenFromAuth(request.getHeader(HttpHeaders.AUTHORIZATION)), "User") as User

    @GetMapping(path = ["/get-one/{userId}"])
    fun getUser(@PathVariable userId: String) : User? = userService.getUser(userId)

    @GetMapping(path = ["/get-all-no-department/{token}"])
    fun getAllUsersNoDepartment(@PathVariable token: String) : List<User> = userService.getAllUsersNoDepartment(token)

    @GetMapping(path = ["/get-all/{token}"])
    fun getAllUsers(@PathVariable token: String) : List<User> = userService.getAllUsers(token)

    @PostMapping(path=["/add-role-to-user"])
    fun addRoleToUser(@RequestBody addRoleToUserDTO: AddRoleToUserDTO) = userService.addRoleToUser(addRoleToUserDTO)

    @PostMapping(path=["/add-user-to-department"])
    fun addUserToDepartment(@RequestBody addUserToDepartment: AddUserToDepartmentDTO, request: HttpServletRequest)
       = userService.addUserToDepartment(addUserToDepartment, tokenService.getTokenFromAuth(request.getHeader(HttpHeaders.AUTHORIZATION)))

    @PostMapping(path=["/delete-user-from-department"])
    fun deleteUserFromDepartment(@RequestBody deleteUserToDepartment: DeleteUserFromDepartmentDTO, request: HttpServletRequest)
    = userService.deleteUserFromDepartment(deleteUserToDepartment, tokenService.getTokenFromAuth(request.getHeader(HttpHeaders.AUTHORIZATION)))

    @PostMapping(path=["/delete-department-from-users"])
    fun deleteDepartmentFromUsers(@RequestBody deleteDepartmentDTO: DeleteDepartmentDTO)
    = userService.deleteDepartmentFromUser(deleteDepartmentDTO)

    @PostMapping(path=["/change-department-name"])
    fun changeUserDepartmentName(@RequestBody changeNameDepartmentDTO: ChangeNameDepartmentDTO) : Boolean
    = userService.changeUserDepartmentName(changeNameDepartmentDTO)
}