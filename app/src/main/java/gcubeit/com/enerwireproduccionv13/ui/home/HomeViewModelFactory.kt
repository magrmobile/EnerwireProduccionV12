package gcubeit.com.enerwireproduccionv13.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gcubeit.com.enerwireproduccionv13.data.repository.machine.MachineRepositoryImpl
import kotlinx.coroutines.DelicateCoroutinesApi

class HomeViewModelFactory @OptIn(DelicateCoroutinesApi::class) constructor(
    private val repository: MachineRepositoryImpl
): ViewModelProvider.NewInstanceFactory() {
    @OptIn(DelicateCoroutinesApi::class)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}