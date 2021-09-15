package gcubeit.com.enerwireproduccionv12.ui.confirm

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import gcubeit.com.enerwireproduccionv12.data.AppApiService
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbStop
import gcubeit.com.enerwireproduccionv12.data.database.entity.SYNC_STATUS_OK
import gcubeit.com.enerwireproduccionv12.data.network.ConnectionLiveData
import gcubeit.com.enerwireproduccionv12.data.network.response.RemoteResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.SimpleResponse
import gcubeit.com.enerwireproduccionv12.data.repository.stop.StopRepositoryImpl
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@DelicateCoroutinesApi
class ConfirmViewModel (
    application: Application
) : AndroidViewModel(application), KodeinAware {
    override val kodein by closestKodein()
    private val apiService: AppApiService by instance()
    private val stopRepository: StopRepositoryImpl by instance()
    private val context by lazy { getApplication<Application>().applicationContext }

    fun localAddStop(stop: DbStop) {
        viewModelScope.launch(Dispatchers.IO) {
            stopRepository.localAddStop(stop)
        }
    }

    fun addStop(stop: DbStop) {
        if(checkNetworkConnection()) {
            val call = apiService.addStop(
                operatorId = stop.operatorId,
                machineId = stop.machineId,
                productId = stop.productId,
                colorId = stop.colorId,
                codeId = stop.codeId,
                conversionId = stop.conversionId,
                quantity = stop.quantity,
                meters = stop.meters,
                comment = stop.comment,
                stopDateTimeStart = stop.stopDatetimeStart,
                stopDateTimeEnd = stop.stopDatetimeEnd
            )
            call.enqueue(object : Callback<RemoteResponse> {
                override fun onFailure(call: Call<RemoteResponse>, t: Throwable) {
                    //Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    /*viewModelScope.launch() {
                        stopRepository.localAddStop(stop)
                    }*/
                    localAddStop(stop)
                }

                override fun onResponse(
                    call: Call<RemoteResponse>,
                    response: Response<RemoteResponse>
                ) {
                    if(response.isSuccessful)  {
                        if(response.body()!!.success) {
                            stop.sync_status = SYNC_STATUS_OK
                            stop.idRemote = response.body()!!.id ?: 0
                            /*viewModelScope.launch() {
                                stopRepository.localAddStop(stop)
                            }*/
                        }
                    }
                    localAddStop(stop)
                }
            })
        } else {
            localAddStop(stop)
        }
    }

    fun localUpdateStop(stop: DbStop) {
        viewModelScope.launch(Dispatchers.IO) {
            stopRepository.localUpdateStop(stop)
        }
        //Toast.makeText(getApplication(), stop.toString(), Toast.LENGTH_LONG).show()
    }

    fun updateStop(stop: DbStop) {
        if(checkNetworkConnection()) {
            if(stop.idRemote > 0) {
                val call = apiService.updateStop(
                    stopId = stop.idRemote,
                    operatorId = stop.operatorId,
                    machineId = stop.machineId,
                    productId = stop.productId,
                    colorId = stop.colorId,
                    codeId = stop.codeId,
                    conversionId = stop.conversionId,
                    quantity = stop.quantity,
                    meters = stop.meters,
                    comment = stop.comment
                )
                call.enqueue(object : Callback<SimpleResponse> {
                    override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                        stop.sync_status = 2
                        localUpdateStop(stop)
                    }

                    override fun onResponse(
                        call: Call<SimpleResponse>,
                        response: Response<SimpleResponse>
                    ) {
                        if (response.isSuccessful) {
                            localUpdateStop(stop)
                        }
                    }
                })
            }
        } else {
            //Toast.makeText(getApplication(), stop.toString(), Toast.LENGTH_LONG).show()
            if(stop.idRemote > 0) {
                stop.sync_status = 2
            }
            localUpdateStop(stop)
        }
    }

    fun checkNetworkConnection(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}