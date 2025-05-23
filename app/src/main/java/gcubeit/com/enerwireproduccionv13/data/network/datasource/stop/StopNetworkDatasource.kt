package gcubeit.com.enerwireproduccionv13.data.network.datasource.stop

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbStop
import gcubeit.com.enerwireproduccionv13.data.network.response.stop.StopsResponse

interface StopNetworkDatasource {
    val downloadedStops: LiveData<StopsResponse>

    suspend fun fetchStops()

    suspend fun deleteStop(stopId: Int)

    suspend fun addStop(stop: DbStop)

    suspend fun updateStop(stop: DbStop)
}