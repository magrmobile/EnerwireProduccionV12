package gcubeit.com.enerwireproduccionv12.data.repository.dashboard

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

class DashboardRepositoryImpl(
): BaseRepository(), DashboardRepository {
}