package gcubeit.com.enerwireproduccionv12.ui.home

import gcubeit.com.enerwireproduccionv12.data.repository.machine.MachineRepositoryImpl
import gcubeit.com.enerwireproduccionv12.ui.base.BaseViewModel
import gcubeit.com.enerwireproduccionv12.util.lazyDeferred

class HomeViewModel(
    private val repository: MachineRepositoryImpl
) : BaseViewModel(repository) {
    val machines by lazyDeferred {
        repository.getMachines()
    }
}