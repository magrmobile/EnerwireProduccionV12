package gcubeit.com.enerwireproduccionv12.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gcubeit.com.enerwireproduccionv12.data.repository.code.CodeRepository
import gcubeit.com.enerwireproduccionv12.data.repository.code.CodeRepositoryImpl
import gcubeit.com.enerwireproduccionv12.data.repository.color.ColorRepositoryImpl
import gcubeit.com.enerwireproduccionv12.data.repository.conversion.ConversionRepositoryImpl
import gcubeit.com.enerwireproduccionv12.data.repository.operator.OperatorRepositoryImpl
import gcubeit.com.enerwireproduccionv12.data.repository.product.ProductRepositoryImpl

class DashboardViewModelFactory(
    private val repository: ConversionRepositoryImpl
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DashboardViewModel(repository) as T
    }
}