package gcubeit.com.enerwireproduccionv12.data.repository.conversion

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.database.DbColorDao
import gcubeit.com.enerwireproduccionv12.data.database.DbConversionDao
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbColor
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbConversion
import gcubeit.com.enerwireproduccionv12.data.network.datasource.color.ColorNetworkDatasource
import gcubeit.com.enerwireproduccionv12.data.network.datasource.conversion.ConversionNetworkDatasource
import gcubeit.com.enerwireproduccionv12.data.network.response.color.ColorsResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.color.asDatabaseModel
import gcubeit.com.enerwireproduccionv12.data.network.response.conversion.ConversionsResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.conversion.asDatabaseModel
import gcubeit.com.enerwireproduccionv12.data.repository.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import java.util.*

class ConversionRepositoryImpl(
    private val dbConversionDao: DbConversionDao,
    private val conversionNetworkDatasource: ConversionNetworkDatasource,
): BaseRepository(), ConversionRepository {
    init {
        conversionNetworkDatasource.downloadedConversions.observeForever { newConversions->
            persistFetchedConversions(newConversions)
        }
    }

    override suspend fun getConversions(): LiveData<out List<DbConversion>> {
        return withContext(Dispatchers.IO){
            initConversionsData()
            return@withContext dbConversionDao.getAllConversions()
        }
    }

    private fun persistFetchedConversions(fetchedConversions: ConversionsResponse){
        GlobalScope.launch(Dispatchers.IO) {
            //dbProductDao.deleteAll()
            dbConversionDao.insertAll(fetchedConversions.conversions.asDatabaseModel())
        }
    }

    private suspend fun initConversionsData() {
        if(isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1)))
            fetchConversions()
    }

    private suspend fun fetchConversions() {
        conversionNetworkDatasource.fetchConversions()
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}