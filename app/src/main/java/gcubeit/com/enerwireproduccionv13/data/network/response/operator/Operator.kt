package gcubeit.com.enerwireproduccionv13.data.network.response.operator


import com.google.gson.annotations.SerializedName
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbOperator

data class Operator(
    val id: Int,
    val name: String,
    val username: String,
    @SerializedName("active_user")
    val activeUser: String,
    val role: String,
    @SerializedName("supervisor_id")
    val supervisorId: Int?,
    @SerializedName("process_id")
    val processId: Int
) {
    override fun toString(): String {
        return if(this.id == -1) {
            name
        } else {
            val nameParts = name.split(" ")
            nameParts[0] + " " + nameParts[1]
        }
    }
}

fun List<Operator>.asDatabaseModel(): List<DbOperator> {
    return map {
        DbOperator (
            id = it.id,
            name = it.name,
            username = it.username,
            activeUser = it.activeUser,
            role = it.role,
            supervisorId = it.supervisorId,
            processId = it.processId
        )
    }
}