package gcubeit.com.enerwireproduccionv12

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import gcubeit.com.enerwireproduccionv12.data.AppApiService
import gcubeit.com.enerwireproduccionv12.data.database.AppDatabase
import gcubeit.com.enerwireproduccionv12.data.database.UserPreferences
import gcubeit.com.enerwireproduccionv12.data.network.ConnectivityInterceptor
import gcubeit.com.enerwireproduccionv12.data.network.ConnectivityInterceptorImpl
import gcubeit.com.enerwireproduccionv12.data.network.datasource.code.CodeNetworkDatasource
import gcubeit.com.enerwireproduccionv12.data.network.datasource.code.CodeNetworkDatasourceImpl
import gcubeit.com.enerwireproduccionv12.data.network.datasource.color.ColorNetworkDatasource
import gcubeit.com.enerwireproduccionv12.data.network.datasource.color.ColorNetworkDatasourceImpl
import gcubeit.com.enerwireproduccionv12.data.network.datasource.conversion.ConversionNetworkDatasource
import gcubeit.com.enerwireproduccionv12.data.network.datasource.conversion.ConversionNetworkDatasourceImpl
import gcubeit.com.enerwireproduccionv12.data.network.datasource.machine.MachineNetworkDatasource
import gcubeit.com.enerwireproduccionv12.data.network.datasource.machine.MachineNetworkDatasourceImpl
import gcubeit.com.enerwireproduccionv12.data.network.datasource.operator.OperatorNetworkDatasource
import gcubeit.com.enerwireproduccionv12.data.network.datasource.operator.OperatorNetworkDatasourceImpl
import gcubeit.com.enerwireproduccionv12.data.network.datasource.product.ProductNetworkDatasource
import gcubeit.com.enerwireproduccionv12.data.network.datasource.product.ProductNetworkDatasourceImpl
import gcubeit.com.enerwireproduccionv12.data.repository.code.CodeRepositoryImpl
import gcubeit.com.enerwireproduccionv12.data.repository.color.ColorRepositoryImpl
import gcubeit.com.enerwireproduccionv12.data.repository.conversion.ConversionRepositoryImpl
import gcubeit.com.enerwireproduccionv12.data.repository.home.HomeRepositoryImpl
import gcubeit.com.enerwireproduccionv12.data.repository.login.LoginRepository
import gcubeit.com.enerwireproduccionv12.data.repository.machine.MachineRepositoryImpl
import gcubeit.com.enerwireproduccionv12.data.repository.operator.OperatorRepositoryImpl
import gcubeit.com.enerwireproduccionv12.data.repository.product.ProductRepositoryImpl
import gcubeit.com.enerwireproduccionv12.ui.dashboard.DashboardViewModelFactory
import gcubeit.com.enerwireproduccionv12.ui.home.HomeViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class EnerwireApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@EnerwireApplication))

        bind() from singleton { CoroutineScope(SupervisorJob()) }
        bind() from singleton { AppDatabase(instance(), instance()) }
        bind() from singleton { instance<AppDatabase>().dbMachineDao() }
        bind() from singleton { instance<AppDatabase>().dbCodeDao() }
        bind() from singleton { instance<AppDatabase>().dbProductDao() }
        bind() from singleton { instance<AppDatabase>().dbOperatorDao() }
        bind() from singleton { instance<AppDatabase>().dbColorDao() }
        bind() from singleton { instance<AppDatabase>().dbConversionDao() }
        bind() from singleton { AppApiService(instance()) }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind<MachineNetworkDatasource>() with singleton { MachineNetworkDatasourceImpl(instance()) }
        bind<CodeNetworkDatasource>() with singleton { CodeNetworkDatasourceImpl(instance()) }
        bind<ProductNetworkDatasource>() with singleton { ProductNetworkDatasourceImpl(instance()) }
        bind<OperatorNetworkDatasource>() with singleton { OperatorNetworkDatasourceImpl(instance()) }
        bind<ColorNetworkDatasource>() with singleton { ColorNetworkDatasourceImpl(instance()) }
        bind<ConversionNetworkDatasource>() with singleton { ConversionNetworkDatasourceImpl(instance()) }
        bind<UserPreferences>() with singleton { UserPreferences(context = applicationContext) }
        bind() from singleton { MachineRepositoryImpl(instance(), instance(),context = applicationContext) }
        bind() from singleton { CodeRepositoryImpl(instance(), instance()) }
        bind() from singleton { ProductRepositoryImpl(instance(), instance()) }
        bind() from singleton { OperatorRepositoryImpl(instance(), instance()) }
        bind() from singleton { ColorRepositoryImpl(instance(), instance()) }
        bind() from singleton { ConversionRepositoryImpl(instance(), instance()) }
        bind<LoginRepository>() with singleton { LoginRepository(instance(), instance()) }
        bind<HomeViewModelFactory>() with provider { HomeViewModelFactory(instance()) }
        bind<DashboardViewModelFactory>() with provider { DashboardViewModelFactory(instance()) }
    }
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}