package gcubeit.com.enerwireproduccionv12

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import gcubeit.com.enerwireproduccionv12.data.AppApiService
import gcubeit.com.enerwireproduccionv12.data.database.AppDatabase
import gcubeit.com.enerwireproduccionv12.data.database.UserPreferences
import gcubeit.com.enerwireproduccionv12.data.network.ConnectivityInterceptor
import gcubeit.com.enerwireproduccionv12.data.network.ConnectivityInterceptorImpl
import gcubeit.com.enerwireproduccionv12.data.network.datasource.machine.MachineNetworkDatasource
import gcubeit.com.enerwireproduccionv12.data.network.datasource.machine.MachineNetworkDatasourceImpl
import gcubeit.com.enerwireproduccionv12.data.repository.LoginRepository
import gcubeit.com.enerwireproduccionv12.data.repository.home.HomeRepository
import gcubeit.com.enerwireproduccionv12.data.repository.home.HomeRepositoryImpl
import gcubeit.com.enerwireproduccionv12.ui.home.HomeViewModelFactory
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

        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { instance<AppDatabase>().dbMachineDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { AppApiService(instance()) }
        bind<MachineNetworkDatasource>() with singleton { MachineNetworkDatasourceImpl(instance()) }
        bind<UserPreferences>() with singleton { UserPreferences(context = applicationContext) }
        bind() from singleton { HomeRepositoryImpl(instance(), instance(),context = applicationContext) }
        bind<LoginRepository>() with singleton { LoginRepository(instance(), instance()) }
        bind<HomeViewModelFactory>() with provider { HomeViewModelFactory(instance()) }
    }
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}