package gcubeit.com.enerwireproduccionv12.data.network.datasource.stop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gcubeit.com.enerwireproduccionv12.data.AppApiService
import gcubeit.com.enerwireproduccionv12.data.network.response.stop.StopsResponse
import gcubeit.com.enerwireproduccionv12.util.NoConnectivityException
import timber.log.Timber

class StopNetworkDatasourceImpl(
    private val appApiService: AppApiService
): StopNetworkDatasource {
    private val _downloadedStops = MutableLiveData<StopsResponse>()
    override val downloadedStops: LiveData<StopsResponse>
        get() = _downloadedStops

    override suspend fun fetchStops(operatorId: Int) {
        //Timber.w(userPreferences.operatorId.first().toString(),"Test")
        try {
            val fetchedStops = appApiService
                .getStops(operatorId)
                .await()
            _downloadedStops.postValue(fetchedStops)
        }catch (e: NoConnectivityException) {
            Timber.e(e, "No Internet Connection.")
        }
    }
}