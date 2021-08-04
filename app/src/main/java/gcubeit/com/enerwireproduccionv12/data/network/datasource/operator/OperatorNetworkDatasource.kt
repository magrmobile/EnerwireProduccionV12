package gcubeit.com.enerwireproduccionv12.data.network.datasource.operator

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.network.response.operator.OperatorsResponse

interface OperatorNetworkDatasource {
    val downloadedOperators: LiveData<OperatorsResponse>

    suspend fun fetchOperators()
}