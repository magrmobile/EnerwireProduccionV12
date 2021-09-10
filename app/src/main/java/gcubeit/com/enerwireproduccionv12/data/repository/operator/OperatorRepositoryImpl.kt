package gcubeit.com.enerwireproduccionv12.data.repository.operator

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.database.DbOperatorDao
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbOperator
import gcubeit.com.enerwireproduccionv12.data.network.datasource.operator.OperatorNetworkDatasource
import gcubeit.com.enerwireproduccionv12.data.network.response.operator.OperatorsResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.operator.asDatabaseModel
import gcubeit.com.enerwireproduccionv12.data.repository.BaseRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

@DelicateCoroutinesApi
class OperatorRepositoryImpl(
    private val dbOperatorDao: DbOperatorDao,
    private val operatorNetworkDatasource: OperatorNetworkDatasource,
): BaseRepository(), OperatorRepository {
    init {
        Handler(Looper.getMainLooper()).post {
            operatorNetworkDatasource.downloadedOperators.observeForever { newOperators ->
                persistFetchedOperators(newOperators)
            }
        }
    }

    suspend fun getOperators(): LiveData<List <DbOperator>> {
        //return withContext(Dispatchers.IO){
            initOperatorsData()
            return dbOperatorDao.getAllOperators()
        //}
    }

    private fun persistFetchedOperators(fetchedOperators: OperatorsResponse){
        GlobalScope.launch(Dispatchers.IO) {
            //dbProductDao.deleteAll()
            dbOperatorDao.insertAll(fetchedOperators.operators.asDatabaseModel())
        }
    }

    private suspend fun initOperatorsData() {
        if(isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1)))
            fetchOperators()
    }

    private suspend fun fetchOperators() {
        operatorNetworkDatasource.fetchOperators()
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

    fun getOperatorsByProcess(machineId: Int): List<DbOperator> = dbOperatorDao.getOperatorsByProcess(machineId)
}