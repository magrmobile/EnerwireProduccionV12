package gcubeit.com.enerwireproduccionv12.data.repository.stop

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import gcubeit.com.enerwireproduccionv12.data.database.DbStopDao
import gcubeit.com.enerwireproduccionv12.data.database.UserPreferences
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbStop
import gcubeit.com.enerwireproduccionv12.data.network.datasource.stop.StopNetworkDatasource
import gcubeit.com.enerwireproduccionv12.data.network.response.stop.StopsResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.stop.asDatabaseModel
import gcubeit.com.enerwireproduccionv12.data.repository.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.ZonedDateTime
import java.util.*

class StopRepositoryImpl(
    private val dbStopDao: DbStopDao,
    private val StopNetworkDatasource: StopNetworkDatasource,
    private val userPreferences: UserPreferences
): BaseRepository(), StopRepository {
    init {
        StopNetworkDatasource.downloadedStops.observeForever { newStops ->
            persistFetchedStops(newStops)
        }
    }

    override suspend fun getStops(): LiveData<out List<DbStop>> {
        return withContext(Dispatchers.IO){
            Timber.d("User OperatorId: " + userPreferences.operatorId.first().toString())
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
            fetchStops(userPreferences.operatorId.first()!!)
    }

    private suspend fun fetchStops(operatorId: Int) {
        //Timber.w(operatorId.toString())
        StopNetworkDatasource.fetchStops(operatorId)
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtySecondsAgo = ZonedDateTime.now().minusSeconds(30)
        return lastFetchTime.isBefore(thirtySecondsAgo)
    }

    override suspend fun getStopsByMachine(machineId: Int): LiveData<out List<DbStop>>{
        return withContext(Dispatchers.IO){
            return@withContext dbStopDao.getStopsByMachine(machineId)
        }
    }
}