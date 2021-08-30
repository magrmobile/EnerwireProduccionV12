package gcubeit.com.enerwireproduccionv12.data.repository.product

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.database.DbProductDao
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbProduct
import gcubeit.com.enerwireproduccionv12.data.network.datasource.product.ProductNetworkDatasource
import gcubeit.com.enerwireproduccionv12.data.network.response.product.ProductsResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.product.asDatabaseModel
import gcubeit.com.enerwireproduccionv12.data.repository.BaseRepository
import kotlinx.coroutines.*
import java.time.ZonedDateTime
import java.util.*

@DelicateCoroutinesApi
class ProductRepositoryImpl(
    private val dbProductDao: DbProductDao,
    private val productNetworkDatasource: ProductNetworkDatasource,
): BaseRepository(), ProductRepository {
    init {
        Handler(Looper.getMainLooper()).post {
            productNetworkDatasource.downloadedProducts.observeForever { newProducts ->
                persistFetchedProducts(newProducts)
            }
        }
    }

    override suspend fun getProducts(): LiveData<out List<DbProduct>> {
        return withContext(Dispatchers.IO){
            initProductsData()
            return@withContext dbProductDao.getAllProducts()
        }
    }

    private fun persistFetchedProducts(fetchedProducts: ProductsResponse){
        GlobalScope.launch(Dispatchers.IO) {
            //dbProductDao.deleteAll()
            dbProductDao.insertAll(fetchedProducts.products.asDatabaseModel())
        }
    }

    private suspend fun initProductsData() {
        if(isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1)))
            fetchProducts()
    }

    private suspend fun fetchProducts() {
        productNetworkDatasource.fetchProducts()
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

    suspend fun getProductsByProcess(processId: Int): List<DbProduct> = dbProductDao.getProductsByProcess(processId)
}