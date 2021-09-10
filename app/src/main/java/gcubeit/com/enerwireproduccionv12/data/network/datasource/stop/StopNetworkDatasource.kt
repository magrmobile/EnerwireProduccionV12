package gcubeit.com.enerwireproduccionv12.data.network.datasource.stop

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbStop
import gcubeit.com.enerwireproduccionv12.data.network.response.RemoteResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.stop.StopsResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

interface StopNetworkDatasource {
    val downloadedStops: LiveData<StopsResponse>

    suspend fun fetchStops()

    suspend fun deleteStop(stopId: Int)

    suspend fun addStop(stop: DbStop)

    suspend fun updateStop(stop: DbStop)
}