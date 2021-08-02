package gcubeit.com.enerwireproduccionv12.data

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import gcubeit.com.enerwireproduccionv12.data.network.ConnectivityInterceptor
import gcubeit.com.enerwireproduccionv12.data.network.response.LoginResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AppApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("logout")
    suspend fun logout(): ResponseBody

    companion object {
        private const val BASE_URL = "http://134.122.113.150/api/"

        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor,
            authToken: String? = null
        ): AppApiService {
            val authInterceptor = Interceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $authToken")
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AppApiService::class.java)
        }
    }
}