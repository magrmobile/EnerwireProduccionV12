package gcubeit.com.enerwireproduccionv12.ui.confirm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbStop
import gcubeit.com.enerwireproduccionv12.data.network.datasource.stop.StopNetworkDatasourceImpl
import gcubeit.com.enerwireproduccionv12.data.repository.stop.StopRepositoryImpl
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

@DelicateCoroutinesApi
class ConfirmViewModel (
    application: Application
) : AndroidViewModel(application), KodeinAware {
    override val kodein by closestKodein()
    private val stopRepository: StopRepositoryImpl by instance()

    fun addStop(stop: DbStop) {
        viewModelScope.launch(Dispatchers.IO) {
            stopRepository.addStop(stop)
        }
    }

    fun localAddStop(stop: DbStop) {
        viewModelScope.launch(Dispatchers.IO) {
            stopRepository.localAddStop(stop)
        }
    }

    fun updateStop(stop: DbStop) {
        viewModelScope.launch(Dispatchers.IO) {
            stopRepository.updateStop(stop)
        }
    }
}