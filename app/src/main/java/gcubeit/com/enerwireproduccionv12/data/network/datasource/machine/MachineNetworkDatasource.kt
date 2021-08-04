package gcubeit.com.enerwireproduccionv12.data.network.datasource.machine

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.network.response.machine.MachinesResponse

interface MachineNetworkDatasource {
    val downloadedMachines: LiveData<MachinesResponse>

    suspend fun fetchMachines(
        deviceId: String
    )
}