package gcubeit.com.enerwireproduccionv13.data.network

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import timber.log.Timber

class ConnectionLiveData(context: Context): LiveData<Boolean>() {
    //private val TAG = "C-Manager"
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    private val validNetworks: MutableSet<Network> = HashSet()

    private fun checkValidNetworks() {
        postValue(validNetworks.size > 0)
    }

    override fun onActive() {
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .build()
        cm.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onInactive() {
        cm.unregisterNetworkCallback(networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            Timber.d( "onAvailable: ${network}")
            val networkCapabilities = cm.getNetworkCapabilities(network)
            val isInternet = networkCapabilities?.hasCapability(NET_CAPABILITY_INTERNET)
            Timber.d( "onAvailable: ${network}, $isInternet")
            if(isInternet == true) {
                validNetworks.add(network)
            }
            checkValidNetworks()
        }

        override fun onLost(network: Network) {
            Timber.d( "onLost: ${network}")
            validNetworks.remove(network)
            checkValidNetworks()
        }
    }
}