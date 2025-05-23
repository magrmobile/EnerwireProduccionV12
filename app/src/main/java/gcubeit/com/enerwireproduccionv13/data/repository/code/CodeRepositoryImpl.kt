package gcubeit.com.enerwireproduccionv13.data.repository.code

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv13.data.database.DbCodeDao
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbCode
import gcubeit.com.enerwireproduccionv13.data.network.datasource.code.CodeNetworkDatasource
import gcubeit.com.enerwireproduccionv13.data.network.response.code.CodesResponse
import gcubeit.com.enerwireproduccionv13.data.network.response.code.asDatabaseModel
import gcubeit.com.enerwireproduccionv13.data.repository.BaseRepository
import kotlinx.coroutines.*
import java.time.ZonedDateTime

@DelicateCoroutinesApi
class CodeRepositoryImpl(
    private val dbCodeDao: DbCodeDao,
    private val codeNetworkDatasource: CodeNetworkDatasource,
): BaseRepository(), CodeRepository {
    //val readCodes: LiveData<List<DbCode>> = dbCodeDao.getAllCodes()

    init {
        Handler(Looper.getMainLooper()).post {
            codeNetworkDatasource.downloadedCodes.observeForever { newCodes ->
                persistFetchedCodes(newCodes)
            }
        }
    }

    override suspend fun getCodes(): LiveData<out List<DbCode>> {
        return withContext(Dispatchers.IO){
            initCodesData()
            return@withContext dbCodeDao.getAllCodes()
        }
    }

    private fun persistFetchedCodes(fetchedCodes: CodesResponse){
        GlobalScope.launch(Dispatchers.IO) {
            //dbCodeDao.deleteAll()
            dbCodeDao.insertAll(fetchedCodes.codes.asDatabaseModel())
        }
    }

    private suspend fun initCodesData() {
        if(isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1)))
            fetchCodes()
    }

    private suspend fun fetchCodes() {
        codeNetworkDatasource.fetchCodes()
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}