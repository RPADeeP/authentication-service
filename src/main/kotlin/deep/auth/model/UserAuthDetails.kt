package deep.auth.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "USER_AUTH_DETAILS")
class UserAuthDetails (
    private var password: String
    )
{
    @Id
    var id: ObjectId = ObjectId.get()
    var code: String = "$id".replace("[a-zA-Z]".toRegex(), "").substring(6,9) + repeat(3){('0'..'9').random()}
}