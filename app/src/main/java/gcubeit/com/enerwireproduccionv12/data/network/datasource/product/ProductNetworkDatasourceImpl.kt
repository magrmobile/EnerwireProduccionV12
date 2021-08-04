package gcubeit.com.enerwireproduccionv12.data.network.datasource.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gcubeit.com.enerwireproduccionv12.data.AppApiService
import gcubeit.com.enerwireproduccionv12.data.network.response.product.ProductsResponse
import gcubeit.com.enerwireproduccionv12.util.NoConnectivityException

class ProductNetworkDatasourceImpl(
    private val appApiService: AppApiService
): ProductNetworkDatasource {
    private val _downloadedProducts = MutableLiveData<ProductsResponse>()
    override val downloadedProducts: LiveData<ProductsResponse>
        get() = _downloadedProducts

    override suspend fun fetchProducts() {
        try {
            val fetchedProducts = appApiService
                .getProducts()
                .await()
            _downloadedProducts.postValue(fetchedProducts)
        }catch (e: NoConnectivityException){
            Log.e("Connectivity", "No Internet Connection.", e)
        }
    }
}