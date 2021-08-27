package gcubeit.com.enerwireproduccionv12.data.database.entity


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import gcubeit.com.enerwireproduccionv12.data.network.response.stop.Stop
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "stop_table")
data class DbStop(
    @PrimaryKey(autoGenerate = false) val id: Int,
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
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
): Parcelable

fun List<DbStop>.asDomainModel(): List<Stop> {
    return map {
        Stop (
            id = it.id,
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
            stopDatetimeEnd = it.stopDatetimeEnd,
            createdAt = it.createdAt,
            updatedAt = it.updatedAt
        )
    }
}