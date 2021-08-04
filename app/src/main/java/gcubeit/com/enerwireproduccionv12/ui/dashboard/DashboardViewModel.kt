package gcubeit.com.enerwireproduccionv12.ui.dashboard

import gcubeit.com.enerwireproduccionv12.data.repository.code.CodeRepository
import gcubeit.com.enerwireproduccionv12.data.repository.code.CodeRepositoryImpl
import gcubeit.com.enerwireproduccionv12.data.repository.color.ColorRepositoryImpl
import gcubeit.com.enerwireproduccionv12.data.repository.conversion.ConversionRepositoryImpl
import gcubeit.com.enerwireproduccionv12.data.repository.operator.OperatorRepositoryImpl
import gcubeit.com.enerwireproduccionv12.data.repository.product.ProductRepositoryImpl
import gcubeit.com.enerwireproduccionv12.ui.base.BaseViewModel
import gcubeit.com.enerwireproduccionv12.util.lazyDeferred

class DashboardViewModel(
    private val repository: ConversionRepositoryImpl
) : BaseViewModel(repository) {

    val data by lazyDeferred {
        repository.getConversions()
    }
}