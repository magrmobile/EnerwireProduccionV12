package gcubeit.com.enerwireproduccionv12.ui.stops

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gcubeit.com.enerwireproduccionv12.data.repository.stop.StopRepositoryImpl
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class StopsViewModelFactory(
    private val repository: StopRepositoryImpl,
    private val machineId: Int? = null
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StopsViewModel(repository, machineId!!) as T
    }
}