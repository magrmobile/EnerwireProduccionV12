package gcubeit.com.enerwireproduccionv13.data.network.datasource.operator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gcubeit.com.enerwireproduccionv13.data.AppApiService
import gcubeit.com.enerwireproduccionv13.data.network.response.operator.OperatorsResponse
import gcubeit.com.enerwireproduccionv13.util.NoConnectivityException
import timber.log.Timber

class OperatorNetworkDatasourceImpl(
    private val appApiService: AppApiService
): OperatorNetworkDatasource {
    private val _downloadedOperators = MutableLiveData<OperatorsResponse>()
    override val downloadedOperators: LiveData<OperatorsResponse>
        get() = _downloadedOperators

    override suspend fun fetchOperators() {
        try {
            val fetchedOperators = appApiService
                .getOperatorsAsync()
                .await()
            _downloadedOperators.postValue(fetchedOperators)
        }catch (e: NoConnectivityException) {
            Timber.e(e, "No Internet Connection.")
        }
    }
}