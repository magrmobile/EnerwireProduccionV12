package gcubeit.com.enerwireproduccionv13.data.repository.operator

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv13.data.database.DbOperatorDao
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbOperator
import gcubeit.com.enerwireproduccionv13.data.network.datasource.operator.OperatorNetworkDatasource
import gcubeit.com.enerwireproduccionv13.data.network.response.operator.OperatorsResponse
import gcubeit.com.enerwireproduccionv13.data.network.response.operator.asDatabaseModel
import gcubeit.com.enerwireproduccionv13.data.repository.BaseRepository
import kotlinx.coroutines.*
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

    override suspend fun getOperators(): LiveData<out List <DbOperator>> {
        return withContext(Dispatchers.IO){
            initOperatorsData()
            return@withContext dbOperatorDao.getAllOperators()
        }
    }

    private fun persistFetchedOperators(fetchedOperators: OperatorsResponse){
        GlobalScope.launch(Dispatchers.IO) {
            //dbProductDao.deleteAll()
            //dbOperatorDao.deleteAll()
            dbOperatorDao.insertAll(fetchedOperators.operators.asDatabaseModel())
        }
    }

    private suspend fun initOperatorsData() {
        //if(isFetchCurrentNeeded(ZonedDateTime.now().minusMinutes(5)))
            fetchOperators()
    }

    private suspend fun fetchOperators() {
        operatorNetworkDatasource.fetchOperators()
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(5)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

    fun getOperatorsByProcess(machineId: Int): List<DbOperator> = dbOperatorDao.getOperatorsByProcess(machineId)
}