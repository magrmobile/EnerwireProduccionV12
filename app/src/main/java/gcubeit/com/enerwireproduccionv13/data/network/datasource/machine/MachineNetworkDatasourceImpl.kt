package gcubeit.com.enerwireproduccionv13.data.network.datasource.machine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gcubeit.com.enerwireproduccionv13.data.AppApiService
import gcubeit.com.enerwireproduccionv13.data.network.response.machine.MachinesResponse
import gcubeit.com.enerwireproduccionv13.util.NoConnectivityException
import timber.log.Timber

class MachineNetworkDatasourceImpl(
    private val appApiService: AppApiService
): MachineNetworkDatasource {
    private val _downloadedMachines = MutableLiveData<MachinesResponse>()
    override val downloadedMachines: LiveData<MachinesResponse>
        get() = _downloadedMachines

    override suspend fun fetchMachines(deviceId: String) {
        try {
            val fetchedMachines = appApiService
                .getMachinesAsync(deviceId)
                .await()
            _downloadedMachines.postValue(fetchedMachines)
        }catch (e: NoConnectivityException) {
            Timber.e(e, "No Internet Connection.")
        }
    }
}