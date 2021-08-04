package gcubeit.com.enerwireproduccionv12.data.network.datasource.conversion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gcubeit.com.enerwireproduccionv12.data.AppApiService
import gcubeit.com.enerwireproduccionv12.data.network.response.color.ColorsResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.conversion.ConversionsResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.operator.OperatorsResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.product.ProductsResponse
import gcubeit.com.enerwireproduccionv12.util.NoConnectivityException

class ConversionNetworkDatasourceImpl(
    private val appApiService: AppApiService
): ConversionNetworkDatasource {
    private val _downloadedConversions = MutableLiveData<ConversionsResponse>()
    override val downloadedConversions: LiveData<ConversionsResponse>
        get() = _downloadedConversions

    override suspend fun fetchConversions() {
        try {
            val fetchedConversions= appApiService
                .getConversions()
                .await()
            _downloadedConversions.postValue(fetchedConversions)
        }catch (e: NoConnectivityException){
            Log.e("Connectivity", "No Internet Connection.", e)
        }
    }
}