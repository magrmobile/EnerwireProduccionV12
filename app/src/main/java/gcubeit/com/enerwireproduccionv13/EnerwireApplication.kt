package gcubeit.com.enerwireproduccionv13

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import gcubeit.com.enerwireproduccionv13.data.AppApiService
import gcubeit.com.enerwireproduccionv13.data.database.*
import gcubeit.com.enerwireproduccionv13.data.network.*
import gcubeit.com.enerwireproduccionv13.data.network.datasource.code.*
import gcubeit.com.enerwireproduccionv13.data.network.datasource.color.*
import gcubeit.com.enerwireproduccionv13.data.network.datasource.conversion.*
import gcubeit.com.enerwireproduccionv13.data.network.datasource.machine.*
import gcubeit.com.enerwireproduccionv13.data.network.datasource.operator.*
import gcubeit.com.enerwireproduccionv13.data.network.datasource.product.*
import gcubeit.com.enerwireproduccionv13.data.network.datasource.stop.*
import gcubeit.com.enerwireproduccionv13.data.repository.BaseRepositoryImpl
import gcubeit.com.enerwireproduccionv13.data.repository.code.CodeRepositoryImpl
import gcubeit.com.enerwireproduccionv13.data.repository.color.ColorRepositoryImpl
import gcubeit.com.enerwireproduccionv13.data.repository.conversion.ConversionRepositoryImpl
import gcubeit.com.enerwireproduccionv13.data.repository.dashboard.DashboardRepositoryImpl
import gcubeit.com.enerwireproduccionv13.data.repository.machine.MachineRepositoryImpl
import gcubeit.com.enerwireproduccionv13.data.repository.operator.OperatorRepositoryImpl
import gcubeit.com.enerwireproduccionv13.data.repository.product.ProductRepositoryImpl
import gcubeit.com.enerwireproduccionv13.data.repository.stop.StopRepositoryImpl
import gcubeit.com.enerwireproduccionv13.ui.create.CreateViewModelFactory
import gcubeit.com.enerwireproduccionv13.ui.dashboard.DashboardViewModelFactory
import gcubeit.com.enerwireproduccionv13.ui.home.HomeViewModelFactory
import kotlinx.coroutines.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*
import timber.log.Timber

@DelicateCoroutinesApi
class EnerwireApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@EnerwireApplication))

        bind() from singleton { CoroutineScope(SupervisorJob()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { instance<AppDatabase>().dbMachineDao() }
        bind() from singleton { instance<AppDatabase>().dbCodeDao() }
        bind() from singleton { instance<AppDatabase>().dbProductDao() }
        bind() from singleton { instance<AppDatabase>().dbOperatorDao() }
        bind() from singleton { instance<AppDatabase>().dbColorDao() }
        bind() from singleton { instance<AppDatabase>().dbConversionDao() }
        bind() from singleton { instance<AppDatabase>().dbStopDao() }
        bind() from singleton { AppApiService(instance()) }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind<MachineNetworkDatasource>() with singleton { MachineNetworkDatasourceImpl(instance()) }
        bind<CodeNetworkDatasource>() with singleton { CodeNetworkDatasourceImpl(instance()) }
        bind<ProductNetworkDatasource>() with singleton { ProductNetworkDatasourceImpl(instance()) }
        bind<OperatorNetworkDatasource>() with singleton { OperatorNetworkDatasourceImpl(instance()) }
        bind<ColorNetworkDatasource>() with singleton { ColorNetworkDatasourceImpl(instance()) }
        bind<ConversionNetworkDatasource>() with singleton { ConversionNetworkDatasourceImpl(instance()) }
        bind() from singleton { StopNetworkDatasourceImpl(instance()) }
        bind<UserPreferences>() with singleton { UserPreferences(instance()) }
        //bind() from singleton { instance<UserPreferences>().operatorId }
        bind() from singleton { BaseRepositoryImpl() }
        bind() from singleton { MachineRepositoryImpl(instance(), instance(), applicationContext) }
        bind() from singleton { CodeRepositoryImpl(instance(), instance()) }
        bind() from singleton { ProductRepositoryImpl(instance(), instance()) }
        bind() from singleton { OperatorRepositoryImpl(instance(), instance()) }
        bind() from singleton { ColorRepositoryImpl(instance(), instance()) }
        bind() from singleton { ConversionRepositoryImpl(instance(), instance()) }
        bind() from singleton { StopRepositoryImpl(instance(), instance(), instance()) }
        bind() from singleton { DashboardRepositoryImpl() }
        bind<HomeViewModelFactory>() with provider { HomeViewModelFactory(instance()) }
        bind<DashboardViewModelFactory>() with provider { DashboardViewModelFactory(instance()) }
        bind<CreateViewModelFactory>() with provider { CreateViewModelFactory(instance()) }
        //bind<StopsViewModelFactory>() with provider { StopsViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}