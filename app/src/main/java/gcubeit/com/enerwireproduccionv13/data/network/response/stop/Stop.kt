package gcubeit.com.enerwireproduccionv13.data.network.response.stop


import com.google.gson.annotations.SerializedName
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbStop

data class Stop(
    @SerializedName("id") val idRemote: Long,
    @SerializedName("machine_id") val machineId: Int,
    @SerializedName("operator_id") val operatorId: Int,
    @SerializedName("product_id") val productId: Int?,
    @SerializedName("color_id") val colorId: Int?,
    @SerializedName("code_id") val codeId: Int,
    @SerializedName("conversion_id") val conversionId: Int?,
    val quantity: Int?,
    val meters: Float?,
    val comment: String?,
    @SerializedName("stop_datetime_start") val stopDatetimeStart: String,
    @SerializedName("stop_datetime_end") val stopDatetimeEnd: String
)

fun List<Stop>.asDatabaseModel(): List<DbStop> {
    return map {
        DbStop (
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