package gcubeit.com.enerwireproduccionv12.ui.edit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class EditViewModelFactory(
    private val application: Application,
    private val machineId: Int? = null,
    private val processId: Int? = null
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditViewModel(application, machineId, processId) as T
    }
}