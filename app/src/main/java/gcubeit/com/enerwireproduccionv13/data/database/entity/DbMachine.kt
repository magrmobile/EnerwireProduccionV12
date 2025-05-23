package gcubeit.com.enerwireproduccionv13.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import gcubeit.com.enerwireproduccionv13.data.network.response.machine.Machine

@Entity(tableName = "machine_table")
data class DbMachine(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @SerializedName("machine_name")
    val machineName: String,
    @SerializedName("process_id")
    val processId: Int,
    val warehouse: String?,
    val lastOperatorId: Int = -1,
    val lastProductId: Int = -1
)

fun List<DbMachine>.asDomainModel(): List<Machine> {
    return map {
        Machine (
            id = it.id,
            machineName = it.machineName,
            processId = it.processId,
            warehouse = it.warehouse!!
        )
    }
}