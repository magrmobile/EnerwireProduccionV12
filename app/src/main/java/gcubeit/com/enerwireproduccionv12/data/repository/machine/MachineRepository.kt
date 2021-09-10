package gcubeit.com.enerwireproduccionv12.data.repository.machine

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbMachine
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbProduct
import kotlinx.coroutines.flow.Flow

interface MachineRepository {
    suspend fun getMachines(): LiveData<out List<DbMachine>>

    suspend fun getLastOperatorId(machineId: Int): Int

    fun updateLastOperatorId(machineId: Int, operatorId: Int)

    suspend fun getLastProductId(machineId: Int): DbProduct

    fun updateLastProductId(machineId: Int, productId: Int)
}