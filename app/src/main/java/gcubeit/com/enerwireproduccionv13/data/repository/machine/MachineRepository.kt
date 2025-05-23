package gcubeit.com.enerwireproduccionv13.data.repository.machine

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbMachine
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbProduct

interface MachineRepository {
    suspend fun getMachines(): LiveData<out List<DbMachine>>

    suspend fun getLastOperatorId(machineId: Int): Int

    fun updateLastOperatorId(machineId: Int, operatorId: Int)

    suspend fun getLastProductId(machineId: Int): DbProduct

    fun updateLastProductId(machineId: Int, productId: Int)
}