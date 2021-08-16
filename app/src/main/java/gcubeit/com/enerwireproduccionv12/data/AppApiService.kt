package gcubeit.com.enerwireproduccionv12.data

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbCode
import gcubeit.com.enerwireproduccionv12.data.network.ConnectivityInterceptor
import gcubeit.com.enerwireproduccionv12.data.network.response.login.LoginResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.code.CodesResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.color.ColorsResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.conversion.ConversionsResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.machine.MachinesResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.operator.OperatorsResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.product.ProductsResponse
import gcubeit.com.enerwireproduccionv12.data.network.response.stop.StopsResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface AppApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("logout")
    suspend fun logout(): ResponseBody

    @GET("dev_machines")
    fun getMachines(
        @Query("serialNumber") serial: String
    ): Deferred<MachinesResponse>

    @GET("codes")
    suspend fun getCodes(): List<DbCode>

    @GET("products")
    fun getProducts(): Deferred<ProductsResponse>

    @GET("operators")
    fun getOperators(): Deferred<OperatorsResponse>

    @GET("colors")
    fun getColors(): Deferred<ColorsResponse>

    @GET("conversions")
    fun getConversions(): Deferred<ConversionsResponse>

    @GET("stops")
    fun getStops(
        @Query("operator_id") operatorId: Int
    ): Deferred<StopsResponse>

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

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(connectivityInterceptor)
                .addInterceptor(loggingInterceptor)
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