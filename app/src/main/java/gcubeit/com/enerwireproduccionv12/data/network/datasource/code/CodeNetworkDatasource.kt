package gcubeit.com.enerwireproduccionv12.data.network.datasource.code

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.network.response.code.CodesResponse

interface CodeNetworkDatasource {
    val downloadedCodes: LiveData<CodesResponse>

    suspend fun fetchCodes()
}