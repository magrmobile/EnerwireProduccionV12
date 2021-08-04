package gcubeit.com.enerwireproduccionv12.data.network.response.color


import com.google.gson.annotations.SerializedName
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbColor
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbOperator
import gcubeit.com.enerwireproduccionv12.data.network.response.operator.Operator

data class Color(
    val id: Int,
    val name: String,
    @SerializedName("hex_code")
    val hexCode: String
)

fun List<Color>.asDatabaseModel(): List<DbColor> {
    return map {
        DbColor (
            id = it.id,
            name = it.name,
            hexCode = it.hexCode
        )
    }
}