package deep.auth.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "USER")
class User(
    var firstName: String,
    var lastName: String,
    var middleName: String,
    var companyToken: String = ""
) : BaseUser {
    @Id
    var id: String = ObjectId.get().toString()
    lateinit var code: String
    lateinit var role: Role
    var department: Department? = null
}