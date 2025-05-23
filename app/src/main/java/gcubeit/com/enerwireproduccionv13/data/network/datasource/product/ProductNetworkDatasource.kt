package gcubeit.com.enerwireproduccionv13.data.network.datasource.product

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv13.data.network.response.product.ProductsResponse

interface ProductNetworkDatasource {
    val downloadedProducts: LiveData<ProductsResponse>

    suspend fun fetchProducts()
}