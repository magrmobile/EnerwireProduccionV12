package gcubeit.com.enerwireproduccionv12.data.repository.stop

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbStop
import gcubeit.com.enerwireproduccionv12.data.database.entity.relations.StopsDetails
import gcubeit.com.enerwireproduccionv12.data.database.views.StopsDashboard
import gcubeit.com.enerwireproduccionv12.data.network.response.RemoteResponse
import retrofit2.Call
import retrofit2.Response

interface StopRepository {
    suspend fun getStops(): LiveData<out List<DbStop>>

    suspend fun getStopsByMachine(machineId: Int): LiveData<List<StopsDetails>>

    suspend fun getStopsDashboard(): LiveData<List<StopsDashboard>>

    suspend fun getLastStopDateTime(machineId: Int): LiveData<String>
}