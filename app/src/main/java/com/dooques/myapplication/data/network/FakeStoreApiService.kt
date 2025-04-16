package com.dooques.myapplication.data.network

import com.dooques.myapplication.model.Product
import com.dooques.myapplication.model.UserProfile
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

class FakeStoreApiProvider() {
    companion object {
        private const val BASE_URL = "https://fakestoreapi.com/"
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

    fun provideFakeStoreDataService(retrofit: Retrofit): FakeStoreApiService =
        retrofit.create(FakeStoreApiService::class.java)
}

interface FakeStoreApiService {
    @GET("products")
    suspend fun getProductList(): List<Product>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Product

    @GET("users")
    suspend fun getUserProfiles(): List<UserProfile>

    @GET("users/{id}")
    suspend fun getUserProfile(@Path("id") id: Int): UserProfile

    @POST("products")
    suspend fun postProductDetails(@Body product: Product)

    @POST("users")
    suspend fun postUserProfile(@Body userProfile: UserProfile)
}

class FakeStoreDataSource(
    private val fakeStoreApiService: FakeStoreApiService,
    private val refreshFlowInterval: Long = 60 * 1_000L
) {

    val productList: Flow<List<Product>> = flow {
        while (true) {
            val productList = fakeStoreApiService.getProductList()
            emit(productList)
            delay(refreshFlowInterval)
        }
    }

    suspend fun getProduct(id: Int): Product = fakeStoreApiService.getProduct(id)

    suspend fun postProductDetails(product: Product) = fakeStoreApiService.postProductDetails(product)

    suspend fun getUserProfiles(): List<UserProfile> = fakeStoreApiService.getUserProfiles()

    suspend fun getUserProfile(id: Int): UserProfile = fakeStoreApiService.getUserProfile(id)

    suspend fun postUserProfile(userProfile: UserProfile) = fakeStoreApiService.postUserProfile(userProfile)

}

