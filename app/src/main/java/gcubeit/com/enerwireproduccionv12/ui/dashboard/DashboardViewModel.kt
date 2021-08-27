package gcubeit.com.enerwireproduccionv12.ui.dashboard

import gcubeit.com.enerwireproduccionv12.data.repository.stop.StopRepositoryImpl
import gcubeit.com.enerwireproduccionv12.ui.base.BaseViewModel
import gcubeit.com.enerwireproduccionv12.util.lazyDeferred
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class DashboardViewModel(
    private val repository: StopRepositoryImpl
) : BaseViewModel(repository) {
    val data by lazyDeferred {
        repository.getStops()
    }

    val dashboardData by lazyDeferred {
        repository.getStopsDashboard()
    }
}