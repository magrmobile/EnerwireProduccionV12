package gcubeit.com.enerwireproduccionv13.data.repository.code

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbCode

interface CodeRepository {
    suspend fun getCodes(): LiveData<out List<DbCode>>
}