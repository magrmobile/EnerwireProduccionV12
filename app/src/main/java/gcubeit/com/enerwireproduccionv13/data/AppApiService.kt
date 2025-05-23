package gcubeit.com.enerwireproduccionv13.data

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import gcubeit.com.enerwireproduccionv13.data.network.ConnectivityInterceptor
import gcubeit.com.enerwireproduccionv13.data.network.response.RemoteResponse
import gcubeit.com.enerwireproduccionv13.data.network.response.SimpleResponse
import gcubeit.com.enerwireproduccionv13.data.network.response.code.CodesResponse
import gcubeit.com.enerwireproduccionv13.data.network.response.color.ColorsResponse
import gcubeit.com.enerwireproduccionv13.data.network.response.conversion.ConversionsResponse
import gcubeit.com.enerwireproduccionv13.data.network.response.login.LoginResponse
import gcubeit.com.enerwireproduccionv13.data.network.response.machine.MachinesResponse
import gcubeit.com.enerwireproduccionv13.data.network.response.operator.OperatorsResponse
import gcubeit.com.enerwireproduccionv13.data.network.response.product.ProductsResponse
import gcubeit.com.enerwireproduccionv13.data.network.response.stop.StopsResponse
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

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
    fun getMachinesAsync(
        @Query("serialNumber") serial: String
    ): Deferred<MachinesResponse>

    @GET("codes")
    fun getCodesAsync(): Deferred<CodesResponse>

    @GET("products")
    fun getProductsAsync(): Deferred<ProductsResponse>

    @GET("operators")
    fun getOperatorsAsync(): Deferred<OperatorsResponse>

    @GET("colors")
    fun getColorsAsync(): Deferred<ColorsResponse>

    @GET("conversions")
    fun getConversionsAsync(): Deferred<ConversionsResponse>

    @GET("stops")
    fun getStopsAsync(
        //@Query("machine_id") machineId: Int
    ): Deferred<StopsResponse>

    @DELETE("stops/{id}")
    suspend fun deleteStop(@Path("id") id: Int) : Response<ResponseBody>

    @POST("stops")
    @Headers("Accept: application/json")
    suspend fun addStop(
        @Query("operator_id") operatorId: Int,
        @Query("machine_id") machineId: Int,
        @Query("product_id") productId: Int?,
        @Query("color_id") colorId: Int?,
        @Query("code_id") codeId: Int,
        @Query("conversion_id") conversionId: Int?,
        @Query("quantity") quantity: Int?,
        @Query("meters") meters: Float?,
        @Query("comment") comment: String?,
        @Query("stop_datetime_start") stopDateTimeStart: String,
        @Query("stop_datetime_end") stopDateTimeEnd: String,
        @Query("type") type: Int = 1
    ): Response<RemoteResponse>

    @PUT("stops/{stop_id}")
    @Headers("Accept: application/json")
    fun updateStop(
        @Path("stop_id") stopId: Long,
        @Query("operator_id") operatorId: Int,
        @Query("machine_id") machineId: Int,
        @Query("product_id") productId: Int?,
        @Query("color_id") colorId: Int?,
        @Query("code_id") codeId: Int,
        @Query("conversion_id") conversionId: Int?,
        @Query("quantity") quantity: Int?,
        @Query("meters") meters: Float?,
        @Query("comment") comment: String?
    ): Call<SimpleResponse>

    companion object {
        //private const val BASE_URL = "http://134.122.113.150/api/"    // Digital Ocean Server
        //private const val BASE_URL = "http://172.16.0.206/api/"         // Raspberry Local
        private const val BASE_URL = "http://192.168.50.8/api/"         // Server Local
        //private const val BASE_URL = "http://192.168.135.54/ew-produccion/public/api/" // Server Local Desarrollo
        //private const val BASE_URL = "http://192.168.56.1/ew-produccion/public/api/" // Server Local Desarrollo

        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): AppApiService {
            /*val authInterceptor = Interceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $authToken")
                    .build()

                return@Interceptor chain.proceed(request)
            }*/

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder()
                //.connectTimeout(10, TimeUnit.SECONDS)
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