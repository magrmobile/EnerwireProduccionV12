package gcubeit.com.enerwireproduccionv12.ui.dashboard

import gcubeit.com.enerwireproduccionv12.data.repository.dashboard.DashboardRepositoryImpl
import gcubeit.com.enerwireproduccionv12.data.repository.stop.StopRepositoryImpl
import gcubeit.com.enerwireproduccionv12.ui.base.BaseViewModel
import gcubeit.com.enerwireproduccionv12.util.lazyDeferred

class DashboardViewModel(
    private val repository: StopRepositoryImpl
) : BaseViewModel(repository) {
    val data by lazyDeferred {
        repository.getStops()
    }
}