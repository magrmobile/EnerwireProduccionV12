package gcubeit.com.enerwireproduccionv12.data.network.response


import com.google.gson.annotations.SerializedName
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbMachine

data class Machine(
    val id: Int,
    @SerializedName("machine_name")
    val machineName: String,
    @SerializedName("process_id")
    val processId: Int,
    val warehouse: String?
)

fun List<Machine>.asDatabaseModel(): List<DbMachine> {
    return map {
        DbMachine (
            id = it.id,
            machineName = it.machineName,
            processId = it.processId,
            warehouse = it.warehouse!!
        )
    }
}