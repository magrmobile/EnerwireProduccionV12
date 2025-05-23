package gcubeit.com.enerwireproduccionv13.data.repository.stop

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbStop
import gcubeit.com.enerwireproduccionv13.data.database.entity.relations.StopsDetails
import gcubeit.com.enerwireproduccionv13.data.database.views.StopsDashboard

interface StopRepository {
    suspend fun getStops(): LiveData<out List<DbStop>>

    suspend fun getStopsByMachine(machineId: Int): LiveData<List<StopsDetails>>

    suspend fun getStopsDashboard(): LiveData<List<StopsDashboard>>

    suspend fun getLastStopDateTime(machineId: Int): LiveData<String>
}