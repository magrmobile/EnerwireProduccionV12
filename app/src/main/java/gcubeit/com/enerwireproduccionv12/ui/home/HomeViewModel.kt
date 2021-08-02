package gcubeit.com.enerwireproduccionv12.ui.home

import gcubeit.com.enerwireproduccionv12.data.repository.home.HomeRepositoryImpl
import gcubeit.com.enerwireproduccionv12.ui.base.BaseViewModel
import gcubeit.com.enerwireproduccionv12.util.lazyDeferred

class HomeViewModel(
    private val homeRepository: HomeRepositoryImpl
) : BaseViewModel(homeRepository) {
    val machines by lazyDeferred {
        homeRepository.getMachines()
    }
}