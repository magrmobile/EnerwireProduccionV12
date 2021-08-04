package gcubeit.com.enerwireproduccionv12.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import gcubeit.com.enerwireproduccionv12.data.network.response.color.Color
import gcubeit.com.enerwireproduccionv12.data.network.response.conversion.Conversion
import gcubeit.com.enerwireproduccionv12.data.network.response.product.Product

@Entity(tableName = "conversion_table")
data class DbConversion(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val description: String,
    val factor: Double,
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