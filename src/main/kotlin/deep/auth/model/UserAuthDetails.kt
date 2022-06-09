package deep.auth.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "USER_AUTH_DETAILS")
class UserAuthDetails (
    @Id
    var id: String,
    private var password: String
    ) : BaseUser
{
    var code: String = "$id".replace("[a-zA-Z]".toRegex(), "").substring(6,9) + (1..3).map { ('0'..'9').random() }.joinToString("")
}