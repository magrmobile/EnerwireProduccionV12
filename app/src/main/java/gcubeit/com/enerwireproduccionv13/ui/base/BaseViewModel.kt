package gcubeit.com.enerwireproduccionv13.ui.base

import androidx.lifecycle.ViewModel
//import gcubeit.com.enerwireproduccionv13.data.AppApiService
import gcubeit.com.enerwireproduccionv13.data.repository.BaseRepository
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext

abstract class BaseViewModel(
    private val repository: BaseRepository
): ViewModel() {
    //suspend fun logout(api: AppApiService) = withContext(Dispatchers.IO){ repository.logout(api) }
}