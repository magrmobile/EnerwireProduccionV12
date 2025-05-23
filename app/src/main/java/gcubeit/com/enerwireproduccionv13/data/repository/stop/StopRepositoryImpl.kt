package gcubeit.com.enerwireproduccionv13.data.repository.stop

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv13.data.AppApiService
import gcubeit.com.enerwireproduccionv13.data.database.DbStopDao
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbStop
import gcubeit.com.enerwireproduccionv13.data.database.entity.relations.StopsDetails
import gcubeit.com.enerwireproduccionv13.data.database.views.StopsDashboard
import gcubeit.com.enerwireproduccionv13.data.network.datasource.stop.StopNetworkDatasource
import gcubeit.com.enerwireproduccionv13.data.repository.BaseRepository
import kotlinx.coroutines.*
import timber.log.Timber

@DelicateCoroutinesApi
class StopRepositoryImpl(
    private val api: AppApiService,
    private val dbStopDao: DbStopDao,
    private val stopNetworkDatasource: StopNetworkDatasource
): BaseRepository(), StopRepository {
    override suspend fun getStops(): LiveData<out List<DbStop>> {
        return withContext(Dispatchers.IO){
            return@withContext dbStopDao.getAllStops()
        }
    }

    override suspend fun getStopsByMachine(machineId: Int): LiveData<List<StopsDetails>>{
        return withContext(Dispatchers.IO){
            return@withContext dbStopDao.getStopsByMachine(machineId)
        }
    }

    override suspend fun getStopsDashboard(): LiveData<List<StopsDashboard>>{
        return withContext(Dispatchers.IO){
            return@withContext dbStopDao.getStopsDashboard()
        }
    }

    override suspend fun getLastStopDateTime(machineId: Int): LiveData<String> {
        return withContext(Dispatchers.IO) {
            return@withContext dbStopDao.getLastStopDateTime(machineId)
        }
    }

    suspend fun localUpdateStop(stop: DbStop) {
        Timber.d(stop.toString())
        return withContext(Dispatchers.IO) {
            return@withContext dbStopDao.updateStop(stop)
        }
    }

    suspend fun localAddStop(stop: DbStop) {
        Timber.d(stop.toString())
        return withContext(Dispatchers.IO) {
            dbStopDao.insertStop(stop)
        }
        //dbStopDao.insertStop(stop)
    }
}