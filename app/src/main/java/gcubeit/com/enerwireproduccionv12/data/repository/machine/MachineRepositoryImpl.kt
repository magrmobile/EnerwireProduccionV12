package gcubeit.com.enerwireproduccionv12.data.repository.machine

import android.content.Context
import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.database.DbMachineDao
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbMachine
import gcubeit.com.enerwireproduccionv12.data.network.datasource.machine.MachineNetworkDatasource
import gcubeit.com.enerwireproduccionv12.data.network.response.machine.MachinesResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.machine.asDatabaseModel
import gcubeit.com.enerwireproduccionv12.data.repository.BaseRepository
import gcubeit.com.enerwireproduccionv12.util.getIMEIDeviceId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import java.util.*

class MachineRepositoryImpl(
    private val dbMachineDao: DbMachineDao,
    private val machineNetworkDatasource: MachineNetworkDatasource,
    context: Context
): BaseRepository(), MachineRepository {
    private val serial = getIMEIDeviceId(context)

    init {
        machineNetworkDatasource.downloadedMachines.observeForever { newMachines ->
            persistFetchedMachines(newMachines)
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
}