package gcubeit.com.enerwireproduccionv12.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import gcubeit.com.enerwireproduccionv12.data.AppApiService
import gcubeit.com.enerwireproduccionv12.data.database.UserPreferences
import gcubeit.com.enerwireproduccionv12.data.network.ConnectivityInterceptor
import gcubeit.com.enerwireproduccionv12.ui.login.LoginActivity
import gcubeit.com.enerwireproduccionv12.util.startNewActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<VM: BaseViewModel>: Fragment(), CoroutineScope, KodeinAware {
    override val kodein by closestKodein()

    protected val userPreferences: UserPreferences by instance()
    protected val connectivityInterceptor: ConnectivityInterceptor by instance()

    protected lateinit var viewModel: VM

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    fun logout() = lifecycleScope.launch {
        val authToken = userPreferences.authToken.first()
        val api = AppApiService(connectivityInterceptor, authToken)
        viewModel.logout(api)
        userPreferences.clear()
        requireActivity().startNewActivity(LoginActivity::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}