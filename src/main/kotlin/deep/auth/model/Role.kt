package deep.auth.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "ROLE")
class Role(
    var name: String,
    var isGeneralStatisticAvailable: Boolean,
    var isProcessCreatorAvailable: Boolean,
    var isJiraAvailable: Boolean,
    var isAddingStaffAvailable: Boolean,
    var companyToken: String = ""
) {
    @Id
    var id: String = ObjectId.get().toString()
}