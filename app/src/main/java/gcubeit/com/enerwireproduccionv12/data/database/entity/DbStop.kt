package gcubeit.com.enerwireproduccionv12.data.database.entity


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import gcubeit.com.enerwireproduccionv12.data.network.response.stop.Stop
import kotlinx.parcelize.Parcelize

const val SYNC_STATUS_OK = 0
const val SYNC_STATUS_FAILED = 1

@Parcelize
@Entity(tableName = "stop_table")
data class DbStop(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @SerializedName("idRemote") var idRemote: Int = 0,
    @SerializedName("machine_id") val machineId: Int,
    @SerializedName("operator_id") val operatorId: Int,
    @SerializedName("product_id") val productId: Int?,
    @SerializedName("color_id") val colorId: Int?,
    @SerializedName("code_id") val codeId: Int,
    @SerializedName("conversion_id")
    val conversionId: Int?,
    val quantity: Int?,
    val meters: Float?,
    val comment: String?,
    @SerializedName("stop_datetime_start") val stopDatetimeStart: String,
    @SerializedName("stop_datetime_end") val stopDatetimeEnd: String,
    var sync_status: Int = SYNC_STATUS_FAILED
): Parcelable

fun List<DbStop>.asDomainModel(): List<Stop> {
    return map {
        Stop (
            idRemote = it.idRemote,
            machineId = it.machineId,
            operatorId = it.operatorId,
            productId = it.productId,
            colorId = it.colorId,
            codeId = it.codeId,
            conversionId = it.conversionId,
            quantity = it.quantity,
            meters = it.meters,
            comment = it.comment,
            stopDatetimeStart = it.stopDatetimeStart,
            stopDatetimeEnd = it.stopDatetimeEnd
        )
    }
}