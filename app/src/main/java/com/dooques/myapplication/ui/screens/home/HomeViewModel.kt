package com.dooques.myapplication.ui.screens.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dooques.myapplication.data.network.FakeStoreRepository
import com.dooques.myapplication.data.offline.OfflineRepository
import com.dooques.myapplication.model.Product
import com.dooques.myapplication.util.HOME_VM_TAG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val fakeStoreDatRepository: FakeStoreRepository,
    private val offlineRepository: OfflineRepository
): ViewModel() {

    private val _productListNetworkState =
        MutableStateFlow<ProductListNetworkState>(ProductListNetworkState.Success(emptyList()))

    val productListNetworkState: StateFlow<ProductListNetworkState> = _productListNetworkState

    init {
        Log.d(HOME_VM_TAG, "\nGetting product data from network...")
        getFakeData()
        Log.d(HOME_VM_TAG, "Product data loaded.")
    }

    private fun getFakeData() {
        Log.d(HOME_VM_TAG, "Returning data from ViewModel...")
        viewModelScope.launch {
            try {
            fakeStoreDatRepository.productList
                .collect { _productListNetworkState.value = ProductListNetworkState.Success(it) }
            Log.d(HOME_VM_TAG, "Data Collected: " +
                    "${(productListNetworkState.value as ProductListNetworkState.Success).products}")
            } catch (e: Exception) {
                _productListNetworkState.value = ProductListNetworkState.Error(e)
            }
        }
    }

    fun getProductsFromJSON(context: Context) = offlineRepository.returnProducts(context)
}

sealed interface ProductListNetworkState {
    data class Success(val products: List<Product>): ProductListNetworkState
    data class Error(val e: Exception): ProductListNetworkState
    object Loading: ProductListNetworkState
}
