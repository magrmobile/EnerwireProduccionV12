package gcubeit.com.enerwireproduccionv12.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import gcubeit.com.enerwireproduccionv12.data.AppApiService
import gcubeit.com.enerwireproduccionv12.data.database.UserPreferences
import gcubeit.com.enerwireproduccionv12.data.network.ConnectivityInterceptor
import gcubeit.com.enerwireproduccionv12.data.network.ConnectivityInterceptorImpl
import gcubeit.com.enerwireproduccionv12.ui.login.LoginActivity
import gcubeit.com.enerwireproduccionv12.util.startNewActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseFragment<VM: BaseViewModel>: Fragment() {
    protected lateinit var userPreferences: UserPreferences
    protected lateinit var connectivityInterceptor: ConnectivityInterceptor
    protected lateinit var viewModel: VM

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userPreferences = UserPreferences(requireContext())
        connectivityInterceptor = ConnectivityInterceptorImpl(requireContext())
    }

    fun logout() = lifecycleScope.launch {
        val authToken = userPreferences.authToken.first()
        val api = AppApiService(connectivityInterceptor, authToken)
        viewModel.logout(api)
        userPreferences.clear()
        requireActivity().startNewActivity(LoginActivity::class.java)
    }
}