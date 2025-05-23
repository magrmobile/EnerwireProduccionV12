package gcubeit.com.enerwireproduccionv13.ui.home

import gcubeit.com.enerwireproduccionv13.data.repository.machine.MachineRepositoryImpl
import gcubeit.com.enerwireproduccionv13.ui.base.BaseViewModel
import gcubeit.com.enerwireproduccionv13.util.lazyDeferred
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class HomeViewModel(
    private val repository: MachineRepositoryImpl
) : BaseViewModel(repository) {
    val machines by lazyDeferred {
        repository.getMachines()
    }
}