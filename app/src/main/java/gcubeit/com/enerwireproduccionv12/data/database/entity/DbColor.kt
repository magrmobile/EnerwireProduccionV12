package gcubeit.com.enerwireproduccionv12.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import gcubeit.com.enerwireproduccionv12.data.network.response.color.Color

@Entity(tableName = "color_table")
data class DbColor(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    @SerializedName("hex_code")
    val hexCode: String
)

fun List<DbColor>.asDomainModel(): List<Color> {
    return map {
        Color (
            id = it.id,
            name = it.name,
            hexCode = it.hexCode
        )
    }
}