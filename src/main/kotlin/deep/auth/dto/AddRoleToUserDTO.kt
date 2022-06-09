package deep.auth.dto

import deep.auth.model.Role
import org.bson.types.ObjectId

data class AddRoleToUserDTO(
    var userId: String,
    var assignRole: Role
    )