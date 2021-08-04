package gcubeit.com.enerwireproduccionv12.data.network.datasource.conversion

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.network.response.conversion.ConversionsResponse

interface ConversionNetworkDatasource {
    val downloadedConversions: LiveData<ConversionsResponse>

    suspend fun fetchConversions()
}