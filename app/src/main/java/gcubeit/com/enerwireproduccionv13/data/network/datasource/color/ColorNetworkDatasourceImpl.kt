package gcubeit.com.enerwireproduccionv13.data.network.datasource.color

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gcubeit.com.enerwireproduccionv13.data.AppApiService
import gcubeit.com.enerwireproduccionv13.data.network.response.color.ColorsResponse
import gcubeit.com.enerwireproduccionv13.util.NoConnectivityException
import timber.log.Timber

class ColorNetworkDatasourceImpl(
    private val appApiService: AppApiService
): ColorNetworkDatasource {
    private val _downloadedColors = MutableLiveData<ColorsResponse>()
    override val downloadedColors: LiveData<ColorsResponse>
        get() = _downloadedColors

    override suspend fun fetchColors() {
        try {
            val fetchedColors= appApiService
                .getColorsAsync()
                .await()
            _downloadedColors.postValue(fetchedColors)
        }catch (e: NoConnectivityException) {
            Timber.e(e, "No Internet Connection.")
        }
    }
}