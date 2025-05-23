package gcubeit.com.enerwireproduccionv13.ui.confirm

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import gcubeit.com.enerwireproduccionv13.data.AppApiService
import gcubeit.com.enerwireproduccionv13.data.database.DbStopDao
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbStop
import gcubeit.com.enerwireproduccionv13.data.database.entity.SYNC_STATUS_OK
import gcubeit.com.enerwireproduccionv13.data.network.response.RemoteResponse
import gcubeit.com.enerwireproduccionv13.data.network.response.SimpleResponse
import gcubeit.com.enerwireproduccionv13.data.repository.stop.StopRepositoryImpl
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
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
    private val stopDao: DbStopDao by instance()
    private val context by lazy { getApplication<Application>().applicationContext }

    suspend fun localAddStop(stop: DbStop) {
        /*viewModelScope.launch(Dispatchers.IO) {
            stopRepository.localAddStop(stop)
        }*/
        stopDao.insertStop(stop)
        //Toast.makeText(getApplication(), stop.toString(), Toast.LENGTH_LONG).show()
    }

    suspend fun addStop(stop: DbStop) {
        val idLocal = stopDao.insertStop(stop)

        if(checkNetworkConnection()) {
            try {
                val response = apiService.addStop(
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

                if(response.isSuccessful) {
                    val remResponse = response.body()
                    if(remResponse != null) {
                        stop.sync_status = SYNC_STATUS_OK
                        stop.idRemote = remResponse.id.toLong()
                        stopDao.updateSyncStatus(idLocal, remResponse.id.toLong())
                    } else {
                        println("Request Error :: " + response.errorBody())
                    }
                } else {
                    println("Request Error :: " + response.errorBody())
                }
            } catch (e: Exception) {
                println("Exception Error :: " + e.message)
            }
        }
    }

    /*fun addStop(stop: DbStop) {
        //var idLocal: Long = 0

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
                override fun onResponse(
                    call: Call<RemoteResponse>,
                    response: Response<RemoteResponse>
                ) {
                    if (response.isSuccessful) {
                        val remResponse: RemoteResponse? = response.body()
                        stop.sync_status = SYNC_STATUS_OK
                        stop.idRemote = remResponse!!.id

                        /*viewModelScope.launch(Dispatchers.Main) {
                            stopRepository.localAddStop(stop)
                        }*/
                        localAddStop(stop)

                        //API response
                        //println(stop)
                    } else {
                        println("Request Error :: " + response.errorBody())
                    }
                }

                override fun onFailure(call: Call<RemoteResponse>, t: Throwable) {
                    viewModelScope.launch(Dispatchers.IO) {
                        stopRepository.localAddStop(stop)
                    }
                    println("Network Error :: " + t.localizedMessage)
                }
            })
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                stopRepository.localAddStop(stop)
            }
            //localAddStop(stop)
            //Toast.makeText(getApplication(), stop.toString(), Toast.LENGTH_LONG).show()
        }
    }*/

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
                    override fun onResponse(
                        call: Call<SimpleResponse>,
                        response: Response<SimpleResponse>
                    ) {
                        if (response.isSuccessful) {
                            stop.sync_status = SYNC_STATUS_OK
                            viewModelScope.launch(Dispatchers.IO) {
                                stopRepository.localUpdateStop(stop)
                            }
                        } else {
                            println("Request Error :: " + response.errorBody())
                        }
                    }

                    override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                        stop.sync_status = 2
                        viewModelScope.launch(Dispatchers.IO) {
                            stopRepository.localUpdateStop(stop)
                        }
                        println("Network Error :: " + t.localizedMessage)
                    }
                })
            }
        } else {
            //Toast.makeText(getApplication(), stop.toString(), Toast.LENGTH_LONG).show()
            if(stop.idRemote > 0) {
                stop.sync_status = 2
            }
            viewModelScope.launch(Dispatchers.IO) {
                stopRepository.localUpdateStop(stop)
            }
        }
    }

    private fun checkNetworkConnection(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}