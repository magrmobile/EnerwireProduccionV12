package gcubeit.com.enerwireproduccionv12.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import gcubeit.com.enerwireproduccionv12.data.network.response.operator.Operator

@Entity(tableName = "operator_table")
data class DbOperator(
    @PrimaryKey(autoGenerate = false)
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
)

fun List<DbOperator>.asDomainModel(): List<Operator> {
    return map {
        Operator (
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