package gcubeit.com.enerwireproduccionv12.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gcubeit.com.enerwireproduccionv12.data.repository.HomeRepository
import gcubeit.com.enerwireproduccionv12.data.repository.LoginRepository

class HomeViewModelFactory(
    private val homeRepository: HomeRepository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(homeRepository) as T
    }
}