package gcubeit.com.enerwireproduccionv13.data.network.response.conversion

import gcubeit.com.enerwireproduccionv13.data.database.entity.DbConversion


data class Conversion(
    val id: Int,
    val description: String,
    val factor: Float,
    val type: String
) {
    override fun toString(): String {
        return description
    }
}

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