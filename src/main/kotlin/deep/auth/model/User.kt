package deep.auth.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "user")
data class User (
    var firstName: String,
    var lastName: String,
    var middleName: String,
    var password: String
) {
    @Id
    var id: ObjectId = ObjectId.get()
    var code: String = ""
}