package gcubeit.com.enerwireproduccionv12.ui.confirm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbStop
import gcubeit.com.enerwireproduccionv12.data.network.datasource.stop.StopNetworkDatasourceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class ConfirmViewModel (
    application: Application
) : AndroidViewModel(application), KodeinAware {
    override val kodein by closestKodein()

    private val stopNetworkDatasource: StopNetworkDatasourceImpl by instance()

    fun addStop(stop: DbStop) {
        viewModelScope.launch(Dispatchers.IO) {
            stopNetworkDatasource.addStop(stop)
        }
    }

    fun updateStop(stop: DbStop) {
        viewModelScope.launch(Dispatchers.IO) {
            stopNetworkDatasource.updateStop(stop)
        }
    }
}