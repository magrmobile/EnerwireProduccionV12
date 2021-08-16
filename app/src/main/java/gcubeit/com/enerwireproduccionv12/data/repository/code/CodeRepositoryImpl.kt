package gcubeit.com.enerwireproduccionv12.data.repository.code

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.database.DbCodeDao
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbCode
import gcubeit.com.enerwireproduccionv12.data.network.datasource.code.CodeNetworkDatasource
import gcubeit.com.enerwireproduccionv12.data.network.response.code.CodesResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.code.asDatabaseModel
import gcubeit.com.enerwireproduccionv12.data.repository.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import java.util.*

class CodeRepositoryImpl(
    private val dbCodeDao: DbCodeDao,
    private val codeNetworkDatasource: CodeNetworkDatasource,
): BaseRepository(), CodeRepository {
    init {
        codeNetworkDatasource.downloadedCodes.observeForever { newCodes ->
            persistFetchedCodes(newCodes)
        }
    }

    override suspend fun getCodes(): LiveData<out List<DbCode>> {
        return withContext(Dispatchers.IO){
            initCodesData()
            return@withContext dbCodeDao.getAllCodes()
        }
    }

    private fun persistFetchedCodes(fetchedCodes: List<DbCode>){
        GlobalScope.launch(Dispatchers.IO) {
            //dbCodeDao.deleteAll()
            dbCodeDao.insertAll(fetchedCodes)
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