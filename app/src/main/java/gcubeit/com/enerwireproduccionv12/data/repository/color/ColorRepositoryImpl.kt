package gcubeit.com.enerwireproduccionv12.data.repository.color

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.database.DbColorDao
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbColor
import gcubeit.com.enerwireproduccionv12.data.network.datasource.color.ColorNetworkDatasource
import gcubeit.com.enerwireproduccionv12.data.network.response.color.ColorsResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.color.asDatabaseModel
import gcubeit.com.enerwireproduccionv12.data.repository.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import java.util.*

class ColorRepositoryImpl(
    private val dbColorDao: DbColorDao,
    private val colorNetworkDatasource: ColorNetworkDatasource,
): BaseRepository(), ColorRepository {
    init {
        colorNetworkDatasource.downloadedColors.observeForever { newColors->
            persistFetchedColors(newColors)
        }
    }

    override suspend fun getColors(): LiveData<out List<DbColor>> {
        return withContext(Dispatchers.IO){
            initColorsData()
            return@withContext dbColorDao.getAllColors()
        }
    }

    private fun persistFetchedColors(fetchedColors: ColorsResponse){
        GlobalScope.launch(Dispatchers.IO) {
            //dbProductDao.deleteAll()
            dbColorDao.insertAll(fetchedColors.colors.asDatabaseModel())
        }
    }

    private suspend fun initColorsData() {
        if(isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1)))
            fetchColors()
    }

    private suspend fun fetchColors() {
        colorNetworkDatasource.fetchColors()
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}