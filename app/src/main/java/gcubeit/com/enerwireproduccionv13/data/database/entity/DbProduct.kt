package gcubeit.com.enerwireproduccionv13.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import gcubeit.com.enerwireproduccionv13.data.network.response.product.Product

@Entity(tableName = "product_table")
data class DbProduct(
    @PrimaryKey(autoGenerate = false) val id: Int,
    @SerializedName("metal_type") val metalType: String,
    @SerializedName("process_id") val processId: Int,
    @SerializedName("product_name") val productName: String,
    val stock: String?
)

fun List<DbProduct>.asDomainModel(): List<Product> {
    return map {
        Product (
            id = it.id,
            metalType = it.metalType,
            productName = it.productName,
            processId = it.processId,
            stock = it.stock
        )
    }
}