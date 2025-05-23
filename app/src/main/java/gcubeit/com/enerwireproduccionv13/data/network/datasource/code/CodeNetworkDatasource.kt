package gcubeit.com.enerwireproduccionv13.data.network.datasource.code

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv13.data.network.response.code.CodesResponse

interface CodeNetworkDatasource {
    val downloadedCodes: LiveData<CodesResponse>

    suspend fun fetchCodes()
}