package com.dooques.myapplication.di

import com.dooques.myapplication.data.network.FakeDataApiService
import com.dooques.myapplication.data.network.FakeDataNetworkRepository
import com.dooques.myapplication.data.network.FakeDataRepository
import com.dooques.myapplication.ui.screens.home.HomeViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit


private const val BASE_URL = "https://fakestoreapi.com/"

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

fun provideFakeDataService(retrofit: Retrofit): FakeDataApiService =
    retrofit.create(FakeDataApiService::class.java)

val networkModule = module {
    factory { provideRetrofit() }
    single { provideFakeDataService(get()) }
}

val repositoryModule = module {
    single<FakeDataRepository> { FakeDataNetworkRepository(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
}