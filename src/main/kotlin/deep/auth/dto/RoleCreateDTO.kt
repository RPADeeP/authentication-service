package deep.auth.dto

class RoleCreateDTO (
    var name: String,
    var isGeneralStatisticAvailable: Boolean,
    var isProcessCreatorAvailable: Boolean,
    var isJiraAvailable: Boolean,
    var isAddingStaffAvailable: Boolean,
    var companyToken: String
)