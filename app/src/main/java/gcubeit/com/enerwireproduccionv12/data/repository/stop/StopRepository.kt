package gcubeit.com.enerwireproduccionv12.data.repository.stop

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbStop

interface StopRepository {
    suspend fun getStops(): LiveData<out List<DbStop>>

    suspend fun getStopsByMachine(machineId: Int): LiveData<out List<DbStop>>
}