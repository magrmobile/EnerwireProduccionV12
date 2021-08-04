package gcubeit.com.enerwireproduccionv12.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gcubeit.com.enerwireproduccionv12.data.repository.machine.MachineRepositoryImpl

class HomeViewModelFactory(
    private val repository: MachineRepositoryImpl
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}