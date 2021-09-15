package gcubeit.com.enerwireproduccionv12.data.repository.stop

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import gcubeit.com.enerwireproduccionv12.data.AppApiService
import gcubeit.com.enerwireproduccionv12.data.database.DbStopDao
import gcubeit.com.enerwireproduccionv12.data.database.UserPreferences
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbStop
import gcubeit.com.enerwireproduccionv12.data.database.entity.SYNC_STATUS_OK
import gcubeit.com.enerwireproduccionv12.data.database.entity.relations.StopsDetails
import gcubeit.com.enerwireproduccionv12.data.database.views.StopsDashboard
import gcubeit.com.enerwireproduccionv12.data.network.datasource.stop.StopNetworkDatasource
import gcubeit.com.enerwireproduccionv12.data.network.response.RemoteResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.stop.StopsResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.stop.asDatabaseModel
import gcubeit.com.enerwireproduccionv12.data.repository.BaseRepository
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.ZonedDateTime
import java.util.*

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
        return withContext(Dispatchers.IO) {
            return@withContext dbStopDao.updateStop(stop)
        }
    }

    suspend fun localAddStop(stop: DbStop) {
        return withContext(Dispatchers.IO) {
            return@withContext dbStopDao.insertStop(stop)
        }
    }


}