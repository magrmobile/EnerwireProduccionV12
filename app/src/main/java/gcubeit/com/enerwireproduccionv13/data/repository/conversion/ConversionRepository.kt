package gcubeit.com.enerwireproduccionv13.data.repository.conversion

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbConversion

interface ConversionRepository {
    suspend fun getConversions(): LiveData<out List<DbConversion>>
}