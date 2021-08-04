package gcubeit.com.enerwireproduccionv12.data.network.datasource.color

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gcubeit.com.enerwireproduccionv12.data.AppApiService
import gcubeit.com.enerwireproduccionv12.data.network.response.color.ColorsResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.operator.OperatorsResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.product.ProductsResponse
import gcubeit.com.enerwireproduccionv12.util.NoConnectivityException

class ColorNetworkDatasourceImpl(
    private val appApiService: AppApiService
): ColorNetworkDatasource {
    private val _downloadedColors = MutableLiveData<ColorsResponse>()
    override val downloadedColors: LiveData<ColorsResponse>
        get() = _downloadedColors

    override suspend fun fetchColors() {
        try {
            val fetchedColors= appApiService
                .getColors()
                .await()
            _downloadedColors.postValue(fetchedColors)
        }catch (e: NoConnectivityException){
            Log.e("Connectivity", "No Internet Connection.", e)
        }
    }
}