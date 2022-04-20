package deep.auth.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "USER")
open class User (
    var firstName: String,
    var lastName: String,
    var middleName: String,
    var companyToken: String? = ""
        ) {
    @Id
    var id: ObjectId = ObjectId.get()
    lateinit var code: String
}