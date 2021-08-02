package gcubeit.com.enerwireproduccionv12.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gcubeit.com.enerwireproduccionv12.data.repository.home.HomeRepository
import gcubeit.com.enerwireproduccionv12.data.repository.home.HomeRepositoryImpl

class HomeViewModelFactory(
    private val homeRepository: HomeRepositoryImpl
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(homeRepository) as T
    }
}