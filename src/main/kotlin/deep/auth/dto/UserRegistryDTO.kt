package deep.auth.dto

data class UserRegistryDTO(
    var firstName: String,
    var lastName: String,
    var middleName: String,
    var password: String,
    var companyToken: String = "",
    var companyName: String
)
