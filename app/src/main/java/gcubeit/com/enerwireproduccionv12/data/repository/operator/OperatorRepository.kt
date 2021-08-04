package gcubeit.com.enerwireproduccionv12.data.repository.operator

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbOperator

interface OperatorRepository {
    suspend fun getOperators(): LiveData<out List<DbOperator>>
}