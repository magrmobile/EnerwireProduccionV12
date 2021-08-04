package gcubeit.com.enerwireproduccionv12.data.network.datasource.color

import androidx.lifecycle.LiveData
import gcubeit.com.enerwireproduccionv12.data.network.response.color.ColorsResponse

interface ColorNetworkDatasource {
    val downloadedColors: LiveData<ColorsResponse>

    suspend fun fetchColors()
}