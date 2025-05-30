package gcubeit.com.enerwireproduccionv13.data.network.datasource.stop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gcubeit.com.enerwireproduccionv13.data.AppApiService
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbStop
import gcubeit.com.enerwireproduccionv13.data.network.response.stop.StopsResponse
import gcubeit.com.enerwireproduccionv13.util.NoConnectivityException
import timber.log.Timber

class StopNetworkDatasourceImpl(
    private val appApiService: AppApiService
): StopNetworkDatasource {
    private val _downloadedStops = MutableLiveData<StopsResponse>()
    override val downloadedStops: LiveData<StopsResponse>
        get() = _downloadedStops

    override suspend fun fetchStops() {
        //Timber.w(userPreferences.operatorId.first().toString(),"Test")
        try {
            val fetchedStops = appApiService
                .getStopsAsync()
                .await()
            _downloadedStops.postValue(fetchedStops)
        } catch (e: NoConnectivityException) {
            Timber.e(e, "No Internet Connection.")
        }
    }

    override suspend fun deleteStop(stopId: Int) {
        try {
            appApiService.deleteStop(stopId)
        } catch (e: NoConnectivityException) {
            Timber.e(e, "No Internet Connection.")
        }
    }

    override suspend fun addStop(stop: DbStop) {
        try {
            appApiService.addStop(
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
        } catch (e: NoConnectivityException) {
            Timber.e(e, "No Internet Connection.")
        }
    }


    override suspend fun updateStop(stop: DbStop) {
        try {
            appApiService.updateStop(
                stopId = stop.id,
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
        } catch (e: NoConnectivityException) {
            Timber.e(e, "No Internet Connection.")
        }
    }
}