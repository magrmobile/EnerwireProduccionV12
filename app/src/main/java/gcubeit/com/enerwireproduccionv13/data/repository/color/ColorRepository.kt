package gcubeit.com.enerwireproduccionv13.data.repository.color

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbColor

interface ColorRepository {
    suspend fun getColors(): LiveData<out List<DbColor>>
}