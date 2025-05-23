package gcubeit.com.enerwireproduccionv13.data.repository.home

import android.content.Context
import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv13.data.database.DbMachineDao
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbMachine
import gcubeit.com.enerwireproduccionv13.data.network.datasource.machine.MachineNetworkDatasource
import gcubeit.com.enerwireproduccionv13.data.network.response.machine.MachinesResponse
import gcubeit.com.enerwireproduccionv13.data.network.response.machine.asDatabaseModel
import gcubeit.com.enerwireproduccionv13.data.repository.BaseRepository
import gcubeit.com.enerwireproduccionv13.util.getIMEIDeviceId
import kotlinx.coroutines.*
import java.time.ZonedDateTime

@DelicateCoroutinesApi
class HomeRepositoryImpl(
    private val dbMachineDao: DbMachineDao,
    private val machineNetworkDatasource: MachineNetworkDatasource,
    context: Context
): BaseRepository(), HomeRepository {
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