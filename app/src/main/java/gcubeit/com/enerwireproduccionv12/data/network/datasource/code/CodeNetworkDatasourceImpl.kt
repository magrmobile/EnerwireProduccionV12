package gcubeit.com.enerwireproduccionv12.data.network.datasource.code

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gcubeit.com.enerwireproduccionv12.data.AppApiService
import gcubeit.com.enerwireproduccionv12.data.network.response.code.CodesResponse
import gcubeit.com.enerwireproduccionv12.util.NoConnectivityException
import timber.log.Timber

class CodeNetworkDatasourceImpl(
    private val appApiService: AppApiService
): CodeNetworkDatasource {
    private val _downloadedCodes = MutableLiveData<CodesResponse>()
    override val downloadedCodes: LiveData<CodesResponse>
        get() = _downloadedCodes

    override suspend fun fetchCodes() {
        try {
            val fetchedCodes = appApiService
                .getCodes()
                .await()
            _downloadedCodes.postValue(fetchedCodes)
        }catch (e: NoConnectivityException) {
            Timber.e(e, "No Internet Connection.")
        }
    }
}