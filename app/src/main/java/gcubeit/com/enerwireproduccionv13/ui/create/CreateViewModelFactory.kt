package gcubeit.com.enerwireproduccionv13.ui.create

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class CreateViewModelFactory(
    private val application: Application,
    private val machineId: Int? = null,
    private val processId: Int? = null
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CreateViewModel(application, machineId, processId) as T
    }
}