package deep.auth.controller

import deep.auth.dto.UserLoginDTO
import deep.auth.dto.UserRegistryDTO
import deep.auth.service.LoginService
import deep.auth.service.RegistrationService
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
@Secured
class AuthenticationController(
    val registrationService: RegistrationService,
    val loginService: LoginService
) {

    @PostMapping(path=["/login"])
    fun login(@RequestBody userLoginDTO: UserLoginDTO) : Boolean {
        return loginService.login(userLoginDTO)
    }

    @PostMapping(path = ["/registry"])
    fun registration(@RequestBody userDTO: UserRegistryDTO) {
        registrationService.registration(userDTO)
    }
}