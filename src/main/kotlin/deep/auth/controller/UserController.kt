package deep.auth.controller

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
    fun addCompanyTokenToUser(@PathVariable token: String, request: HttpServletRequest){
        userService.addCompanyTokenToUser(token,
            tokenService.parseToken(tokenService.getTokenFromAuth(request.getHeader(HttpHeaders.AUTHORIZATION)), "User") as User
        )
    }

    @GetMapping(path = ["/get-one"])
    fun getUser(request: HttpServletRequest) : User {
        return tokenService.parseToken(tokenService.getTokenFromAuth(request.getHeader(HttpHeaders.AUTHORIZATION)), "User") as User
    }

    @GetMapping(path = ["/get-all/{token}"])
    fun getUser(@PathVariable token: String) : List<User> {
        return userService.getAllUsersForCompany(token)
    }

}