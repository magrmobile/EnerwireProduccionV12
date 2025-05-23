package gcubeit.com.enerwireproduccionv13.data.network.datasource.conversion

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv13.data.network.response.conversion.ConversionsResponse

interface ConversionNetworkDatasource {
    val downloadedConversions: LiveData<ConversionsResponse>

    suspend fun fetchConversions()
}