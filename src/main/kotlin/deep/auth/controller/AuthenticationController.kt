package deep.auth.controller

import deep.auth.dto.TokenCallBack
import deep.auth.dto.UserLoginDTO
import deep.auth.dto.UserRegistryDTO
import deep.auth.service.LoginService
import deep.auth.service.RegistrationService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthenticationController(
    val registrationService: RegistrationService,
    val loginService: LoginService
) {

    @PostMapping(path=["/login"])
    fun login(@RequestBody userLoginDTO: UserLoginDTO) : TokenCallBack {
        return loginService.login(userLoginDTO)
    }

    @PostMapping(path = ["/registry"])
    fun registration(@RequestBody userDTO: UserRegistryDTO): TokenCallBack {
        return registrationService.registration(userDTO)
    }
}