package gcubeit.com.enerwireproduccionv12.ui.stops

import androidx.lifecycle.viewModelScope
import gcubeit.com.enerwireproduccionv12.data.repository.stop.StopRepositoryImpl
import gcubeit.com.enerwireproduccionv12.ui.base.BaseViewModel
import gcubeit.com.enerwireproduccionv12.util.lazyDeferred
import kotlinx.coroutines.launch

class StopsViewModel(
    private val repository: StopRepositoryImpl,
    private val machineId: Int
) : BaseViewModel(repository) {
    /*val data by lazyDeferred {
        repository.getStops()
    }*/

    val machineData by lazyDeferred {
        repository.getStopsByMachine(machineId)
    }
}