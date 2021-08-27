package gcubeit.com.enerwireproduccionv12.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import gcubeit.com.enerwireproduccionv12.data.network.response.conversion.Conversion

@Entity(tableName = "conversion_table")
data class DbConversion(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val description: String,
    val factor: Float,
    val type: String
)

fun List<DbConversion>.asDomainModel(): List<Conversion> {
    return map {
        Conversion (
            id = it.id,
            description = it.description,
            factor = it.factor,
            type = it.type
        )
    }
}