package deep.auth.service

import deep.auth.dto.RoleCreateDTO
import deep.auth.model.Role
import deep.auth.repository.RoleRepository
import org.springframework.stereotype.Service

interface RoleService {
    fun createRole(roleCreateDTO: RoleCreateDTO)
    fun getAllRoles(companyToken: String) : List<Role>
}

@Service
class RoleServiceImpl(
    private val roleRepository: RoleRepository
) : RoleService {
    override fun createRole(roleCreateDTO: RoleCreateDTO) {
        val role = Role(
            roleCreateDTO.name,
            roleCreateDTO.isGeneralStatisticAvailable,
            roleCreateDTO.isProcessCreatorAvailable,
            roleCreateDTO.isJiraAvailable,
            roleCreateDTO.isAddingStaffAvailable,
            roleCreateDTO.companyToken
        )
        roleRepository.save(role)
    }

    override fun getAllRoles(companyToken: String): List<Role> {
        return roleRepository.findByCompanyToken(companyToken) ?: emptyList()
    }

}