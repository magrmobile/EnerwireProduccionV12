package gcubeit.com.enerwireproduccionv13.data.repository.machine

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv13.data.database.DbMachineDao
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbMachine
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbProduct
import gcubeit.com.enerwireproduccionv13.data.network.datasource.machine.MachineNetworkDatasource
import gcubeit.com.enerwireproduccionv13.data.network.response.machine.MachinesResponse
import gcubeit.com.enerwireproduccionv13.data.network.response.machine.asDatabaseModel
import gcubeit.com.enerwireproduccionv13.data.repository.BaseRepository
import gcubeit.com.enerwireproduccionv13.util.getIMEIDeviceId
import kotlinx.coroutines.*
import java.time.ZonedDateTime

@DelicateCoroutinesApi
class MachineRepositoryImpl(
    private val dbMachineDao: DbMachineDao,
    private val machineNetworkDatasource: MachineNetworkDatasource,
    context: Context,
    private val machineId: Int? = null
): BaseRepository(), MachineRepository {
    private val serial = getIMEIDeviceId(context)

    init {
        Handler(Looper.getMainLooper()).post {
            machineNetworkDatasource.downloadedMachines.observeForever { newMachines ->
                persistFetchedMachines(newMachines)
            }
        }
        //Toast.makeText(context, serial, Toast.LENGTH_SHORT).show()
    }

    override suspend fun getMachines(): LiveData<out List<DbMachine>> {
        return withContext(Dispatchers.IO){
            initMachinesData()
            return@withContext dbMachineDao.getAllMachines()
        }
    }

    private fun persistFetchedMachines(fetchedMachines: MachinesResponse){
        GlobalScope.launch(Dispatchers.IO) {
            dbMachineDao.deleteAll()
            dbMachineDao.insertAll(fetchedMachines.machines.asDatabaseModel())
        }
    }

    private suspend fun initMachinesData() {
        if(isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1)))
            fetchMachines()
    }

    private suspend fun fetchMachines() {
        machineNetworkDatasource.fetchMachines(serial)
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

    override suspend fun getLastOperatorId(machineId: Int): Int {
        return withContext(Dispatchers.IO) {
            return@withContext dbMachineDao.getLastOperatorId(machineId)
        }
    }

    override fun updateLastOperatorId(machineId: Int, operatorId: Int) {
        /*return withContext(Dispatchers.IO) {
            return@withContext dbMachineDao.updateOperatorId(machineId, operatorId)
        }*/
        dbMachineDao.updateOperatorId(machineId, operatorId)
    }

    override suspend fun getLastProductId(machineId: Int): DbProduct {
        return withContext(Dispatchers.IO) {
            return@withContext dbMachineDao.getLastProductId(machineId)
        }
    }

    override fun updateLastProductId(machineId: Int, productId: Int) {
        /*return withContext(Dispatchers.IO) {
            return@withContext dbMachineDao.updateOperatorId(machineId, operatorId)
        }*/
        dbMachineDao.updateProductId(machineId, productId)
    }
}