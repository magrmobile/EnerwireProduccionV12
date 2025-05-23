package gcubeit.com.enerwireproduccionv13.ui.stops

import gcubeit.com.enerwireproduccionv13.data.repository.stop.StopRepositoryImpl
import gcubeit.com.enerwireproduccionv13.ui.base.BaseViewModel
import gcubeit.com.enerwireproduccionv13.util.lazyDeferred
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class StopsViewModel(
    private val repository: StopRepositoryImpl,
    private val machineId: Int
) : BaseViewModel(repository) {
    /*private val _stopsData = MutableLiveData<List<StopsDetails>>()
    val stopsData: LiveData<List<StopsDetails>> get() = _stopsData

    fun onCreate() {
        viewModelScope.launch(Dispatchers.Main) {
            _stopsData.value = repository.getStopsByMachine(machineId).value
        }
    }*/
    val stopsDataAll by lazyDeferred {
        repository.getStops()
    }

    val stopsData by lazyDeferred {
        repository.getStopsByMachine(machineId)
    }
}