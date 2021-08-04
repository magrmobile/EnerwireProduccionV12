package gcubeit.com.enerwireproduccionv12.data.repository.product

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbProduct

interface ProductRepository {
    suspend fun getProducts(): LiveData<out List<DbProduct>>
}