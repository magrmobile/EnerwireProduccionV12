package gcubeit.com.enerwireproduccionv12.data.network.datasource.conversion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gcubeit.com.enerwireproduccionv12.data.AppApiService
import gcubeit.com.enerwireproduccionv12.data.network.response.conversion.ConversionsResponse
import gcubeit.com.enerwireproduccionv12.util.NoConnectivityException
import timber.log.Timber

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
        }catch (e: NoConnectivityException) {
            Timber.e(e, "No Internet Connection.")
        }
    }
}