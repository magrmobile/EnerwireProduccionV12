package gcubeit.com.enerwireproduccionv12.data.network.datasource.stop

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.network.response.stop.StopsResponse

interface StopNetworkDatasource {
    val downloadedStops: LiveData<StopsResponse>

    suspend fun fetchStops(operatorId: Int)
}