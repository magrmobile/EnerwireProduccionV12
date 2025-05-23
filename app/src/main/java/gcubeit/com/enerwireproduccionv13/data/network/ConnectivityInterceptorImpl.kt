package gcubeit.com.enerwireproduccionv13.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import gcubeit.com.enerwireproduccionv13.util.NoConnectionException
import gcubeit.com.enerwireproduccionv13.util.NoConnectivityException
import gcubeit.com.enerwireproduccionv13.util.SlowConnectionException
import kotlinx.coroutines.flow.channelFlow
import okhttp3.Interceptor
import okhttp3.Response
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*

class ConnectivityInterceptorImpl(
    context: Context,
    private val timeout: Long = 10L // Tiempo de espera en segundos
): ConnectivityInterceptor {
    private val appContext = context.applicationContext
    private val wifiManager = appContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isOnline()) {
            throw NoConnectivityException()
        }

        val request = chain.request()

        // Temporizador de espera para enviar la solicitud
        val sendTimer = Timer()
        sendTimer.schedule(object : TimerTask() {
            override fun run() {
                try {
                    chain.proceed(request)
                } catch (e: Exception) {
                    throw e
                }
            }
        }, timeout * 1000)

        try {
            val response = chain.proceed(request)

            // Temporizador de espera despues de recibir la respuesta
            val receiverTime = Timer()
            receiverTime.schedule(object : TimerTask() {
                override fun run() {
                    response.close()
                }
            }, timeout * 1000)

            return response
        } catch (e: SocketTimeoutException) {
            throw SlowConnectionException()
        } catch (e: UnknownHostException) {
            throw NoConnectionException()
        } catch (e: Exception) {
            throw e
        } finally {
            // Cancelar los temporizadores para liberar recursos
            sendTimer.cancel()
        }
    }
    @Suppress("DEPRECATION")
    private fun isOnline(): Boolean {
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}