package gcubeit.com.enerwireproduccionv12.data.repository.home

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbMachine

interface HomeRepository {
    suspend fun getMachines(): LiveData<out List<DbMachine>>
}