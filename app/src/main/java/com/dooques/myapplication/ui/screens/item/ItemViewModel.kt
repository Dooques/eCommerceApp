package com.dooques.myapplication.ui.screens.item

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dooques.myapplication.data.network.FakeStoreRepository
import com.dooques.myapplication.data.offline.OfflineRepository
import com.dooques.myapplication.model.Product
import com.dooques.myapplication.util.ITEM_VM_TAG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ItemViewModel(
    private val fakeStoreDataRepository: FakeStoreRepository,
    private val offlineRepository: OfflineRepository
): ViewModel() {

    private val _productState = MutableStateFlow<ProductNetworkState>(ProductNetworkState.Loading)
    val productState: StateFlow<ProductNetworkState> = _productState

    fun getProduct(id: Int) {
        Log.d(ITEM_VM_TAG, "\nGetting product data...")
        viewModelScope.launch {
            try {
            _productState.value = ProductNetworkState.Success(fakeStoreDataRepository.getProduct(id))
            Log.d(ITEM_VM_TAG, "Product: ${_productState.value}")
            } catch (e: Exception) {
                _productState.value = ProductNetworkState.Error(e)
            }
        }

    }

    fun getProductFromJSON(context: Context, id: Int) =
        offlineRepository.returnProducts(context).first {it.id == id}

    fun clearProductState() {
        _productState.value = ProductNetworkState.Success(Product())
    }
}

sealed interface ProductNetworkState {
    data class Success(val product: Product): ProductNetworkState
    data class Error(val e: Exception): ProductNetworkState
    object Loading: ProductNetworkState
}
