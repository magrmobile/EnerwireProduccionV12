package gcubeit.com.enerwireproduccionv12.data.network.response.conversion

import gcubeit.com.enerwireproduccionv12.data.database.entity.DbConversion


data class Conversion(
    val id: Int,
    val description: String,
    val factor: Double,
    val type: String
)

fun List<Conversion>.asDatabaseModel(): List<DbConversion> {
    return map {
        DbConversion (
            id = it.id,
            description = it.description,
            factor = it.factor,
            type = it.type
        )
    }
}