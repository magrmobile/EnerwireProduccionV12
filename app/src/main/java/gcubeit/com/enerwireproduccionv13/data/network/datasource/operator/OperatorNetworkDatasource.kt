package gcubeit.com.enerwireproduccionv13.data.network.datasource.operator

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv13.data.network.response.operator.OperatorsResponse

interface OperatorNetworkDatasource {
    val downloadedOperators: LiveData<OperatorsResponse>

    suspend fun fetchOperators()
}