package deep.auth.controller

import deep.auth.model.UserAuthDetails
import deep.auth.service.TokenService
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/token")
class TokenController(
    val tokenService: TokenService
    ) {

    @GetMapping(path=["/authenticate"])
    fun authenticate(request: HttpServletRequest) : String {
        return (tokenService.parseToken(tokenService.getTokenFromAuth(request.getHeader(HttpHeaders.AUTHORIZATION)), "UserAuth") as UserAuthDetails?)?.code ?: ""
    }
}