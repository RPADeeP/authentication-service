package deep.auth.controller

import deep.auth.dto.RoleCreateDTO
import deep.auth.model.Role
import deep.auth.service.RoleService
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/role")
@Secured
class RoleController(
    val roleService: RoleService
) {
    @PostMapping(path=["/create"])
    fun create(@RequestBody roleCreateDTO: RoleCreateDTO) {
        return roleService.createRole(roleCreateDTO)
    }

    @GetMapping(path=["/get-all/{token}"])
    fun getAllRoles(@PathVariable token: String) : List<Role> {
        return roleService.getAllRoles(token)
    }
}