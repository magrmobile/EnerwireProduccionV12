package gcubeit.com.enerwireproduccionv12.ui.edit

import android.app.Application
import androidx.lifecycle.*
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbOperator
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbProduct
import gcubeit.com.enerwireproduccionv12.data.repository.code.CodeRepositoryImpl
import gcubeit.com.enerwireproduccionv12.data.repository.color.ColorRepositoryImpl
import gcubeit.com.enerwireproduccionv12.data.repository.conversion.ConversionRepositoryImpl
import gcubeit.com.enerwireproduccionv12.data.repository.operator.OperatorRepositoryImpl
import gcubeit.com.enerwireproduccionv12.data.repository.product.ProductRepositoryImpl
import gcubeit.com.enerwireproduccionv12.data.repository.stop.StopRepositoryImpl
import gcubeit.com.enerwireproduccionv12.util.lazyDeferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

@DelicateCoroutinesApi
class EditViewModel(
    application: Application,
    private val machineId: Int? = null
) : AndroidViewModel(application), KodeinAware {
    override val kodein by closestKodein()

    private val codeRepository: CodeRepositoryImpl by instance()
    private val colorRepository: ColorRepositoryImpl by instance()
    private val productRepository: ProductRepositoryImpl by instance()
    private val operatorRepository: OperatorRepositoryImpl by instance()
    private val conversionRepository: ConversionRepositoryImpl by instance()
    private val stopRepository: StopRepositoryImpl by instance()

    private val _operators = MutableLiveData<List<DbOperator>>()
    val operators : LiveData<List<DbOperator>> get() = _operators

    private val _products = MutableLiveData<List<DbProduct>>()
    val products : LiveData<List<DbProduct>> get() = _products

    init {
        viewModelScope.launch(Dispatchers.Main) {
            _operators.value = operatorRepository.getOperators().value
            _products.value = productRepository.getProducts().value
        }
    }

    val codes by lazyDeferred { codeRepository.getCodes() }
    val colors by lazyDeferred { colorRepository.getColors() }
    val conversions by lazyDeferred { conversionRepository.getConversions() }
    val productsByProcess by lazyDeferred { productRepository.getProductsByProcess(machineId!!) }
    val operatorsByProcess by lazyDeferred { operatorRepository.getOperatorsByProcess(machineId!!) }

    val lastStopDateTime = stopRepository.getLastStopDateTime(machineId!!)
}