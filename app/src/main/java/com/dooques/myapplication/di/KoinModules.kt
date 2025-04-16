package com.dooques.myapplication.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dooques.myapplication.data.datastore.UserDatastoreRepository
import com.dooques.myapplication.data.datastore.UserDsRepository
import com.dooques.myapplication.data.network.FakeStoreNetworkRepository
import com.dooques.myapplication.data.network.FakeStoreApiProvider
import com.dooques.myapplication.data.network.FakeStoreRepository
import com.dooques.myapplication.data.network.FakeStoreDataSource
import com.dooques.myapplication.ui.screens.account.AccountViewModel
import com.dooques.myapplication.ui.screens.home.HomeViewModel
import com.dooques.myapplication.ui.screens.item.ItemViewModel
import com.dooques.myapplication.ui.screens.selling.CreateListingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
private val fakeStoreService = FakeStoreApiProvider()
private fun provideRetrofit() = fakeStoreService.provideRetrofit()
private fun provideFakeStoreDataService() = fakeStoreService.provideFakeStoreDataService(provideRetrofit())

val networkModule = module {
    factory { provideRetrofit() }
    single { provideFakeStoreDataService() }
    single<FakeStoreDataSource> { FakeStoreDataSource(get()) }
}

val repositoryModule = module {
    single<FakeStoreRepository> { FakeStoreNetworkRepository(get()) }
    single<UserDsRepository> { UserDatastoreRepository(androidContext().dataStore) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { ItemViewModel(get()) }
    viewModel { CreateListingViewModel(get()) }
    viewModel { AccountViewModel(get(), get()) }
}