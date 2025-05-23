package gcubeit.com.enerwireproduccionv13.data.network.response.product


import com.google.gson.annotations.SerializedName
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbProduct

data class Product(
    val id: Int,
    @SerializedName("metal_type") val metalType: String,
    @SerializedName("process_id") val processId: Int,
    @SerializedName("product_name") val productName: String,
    val stock: String?
) {
    override fun toString(): String {
        return productName
    }
}

fun List<Product>.asDatabaseModel(): List<DbProduct> {
    return map {
        DbProduct (
            id = it.id,
            metalType = it.metalType,
            processId = it.processId,
            productName = it.productName,
            stock = it.stock
        )
    }
}