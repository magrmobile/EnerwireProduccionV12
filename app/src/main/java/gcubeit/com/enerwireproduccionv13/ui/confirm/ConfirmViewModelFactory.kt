package gcubeit.com.enerwireproduccionv13.ui.confirm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class ConfirmViewModelFactory(
    private val application: Application
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ConfirmViewModel(application) as T
    }
}