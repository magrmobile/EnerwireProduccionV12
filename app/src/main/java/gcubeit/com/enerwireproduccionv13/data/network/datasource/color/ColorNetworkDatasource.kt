package gcubeit.com.enerwireproduccionv13.data.network.datasource.color

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv13.data.network.response.color.ColorsResponse

interface ColorNetworkDatasource {
    val downloadedColors: LiveData<ColorsResponse>

    suspend fun fetchColors()
}