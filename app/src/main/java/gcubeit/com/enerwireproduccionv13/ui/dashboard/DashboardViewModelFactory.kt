package gcubeit.com.enerwireproduccionv13.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gcubeit.com.enerwireproduccionv13.data.repository.stop.StopRepositoryImpl
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class DashboardViewModelFactory(
    private val repository: StopRepositoryImpl
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DashboardViewModel(repository) as T
    }
}