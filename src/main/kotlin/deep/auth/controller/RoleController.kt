package deep.auth.controller

import deep.auth.dto.RoleCreateDTO
import deep.auth.model.Role
import deep.auth.service.RoleService
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/role")
@Secured
class RoleController(
    val roleService: RoleService
) {
    @PostMapping(path=["/create"])
    fun create(@RequestBody roleCreateDTO: RoleCreateDTO) = roleService.createRole(roleCreateDTO)

    @GetMapping(path=["/get-all/{token}"])
    fun getAllRoles(@PathVariable token: String) : List<Role> = roleService.getAllRoles(token)
}