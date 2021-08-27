package gcubeit.com.enerwireproduccionv12.data.repository.stop

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.database.DbStopDao
import gcubeit.com.enerwireproduccionv12.data.database.UserPreferences
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbStop
import gcubeit.com.enerwireproduccionv12.data.database.entity.relations.StopsDetails
import gcubeit.com.enerwireproduccionv12.data.database.views.StopsDashboard
import gcubeit.com.enerwireproduccionv12.data.network.datasource.stop.StopNetworkDatasource
import gcubeit.com.enerwireproduccionv12.data.network.response.stop.StopsResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.stop.asDatabaseModel
import gcubeit.com.enerwireproduccionv12.data.repository.BaseRepository
import kotlinx.coroutines.*
import java.time.ZonedDateTime
import java.util.*

@DelicateCoroutinesApi
class StopRepositoryImpl(
    private val dbStopDao: DbStopDao,
    private val stopNetworkDatasource: StopNetworkDatasource,
    private val userPreferences: UserPreferences
): BaseRepository(), StopRepository {
    init {
        Handler(Looper.getMainLooper()).post {
            stopNetworkDatasource.downloadedStops.observeForever { newStops ->
                persistFetchedStops(newStops)
            }
        }
    }

    override suspend fun getStops(): LiveData<out List<DbStop>> {
        return withContext(Dispatchers.IO){
            //Timber.d("User OperatorId: " + userPreferences.operatorId.first().toString())
            initStopsData()
            return@withContext dbStopDao.getAllStops()
        }
    }

    private fun persistFetchedStops(fetchedStops: StopsResponse){
        GlobalScope.launch(Dispatchers.IO) {
            dbStopDao.deleteAll()
            dbStopDao.insertAll(fetchedStops.stops.asDatabaseModel())
        }
    }

    private suspend fun initStopsData() {
        if(isFetchCurrentNeeded(ZonedDateTime.now().minusMinutes(1)))
            fetchStops()
    }

    private suspend fun fetchStops() {
        //Timber.w(operatorId.toString())
        stopNetworkDatasource.fetchStops()
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtySecondsAgo = ZonedDateTime.now().minusSeconds(30)
        return lastFetchTime.isBefore(thirtySecondsAgo)
    }

    suspend fun getStopsByMachine(machineId: Int): LiveData<List<StopsDetails>>{
        return withContext(Dispatchers.IO){
            return@withContext dbStopDao.getStopsByMachine(machineId)
        }
    }

    suspend fun getStopsDashboard(): LiveData<List<StopsDashboard>>{
        return withContext(Dispatchers.IO){
            return@withContext dbStopDao.getStopsDashboard()
        }
    }

    //fun getStopsByMachine(machineId: Int): LiveData<List<StopsDetails>> = dbStopDao.getStopsByMachine(machineId)


    fun getLastStopDateTime(machineId: Int): LiveData<String> = dbStopDao.getLastStopDateTime(machineId)
}