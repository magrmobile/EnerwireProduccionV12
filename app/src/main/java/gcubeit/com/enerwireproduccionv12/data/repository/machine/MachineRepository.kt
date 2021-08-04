package gcubeit.com.enerwireproduccionv12.data.repository.machine

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbMachine

interface MachineRepository {
    suspend fun getMachines(): LiveData<out List<DbMachine>>
}