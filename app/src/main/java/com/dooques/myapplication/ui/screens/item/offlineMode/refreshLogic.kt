package com.dooques.myapplication.ui.screens.item.offlineMode

import com.dooques.myapplication.model.Product
import com.dooques.myapplication.ui.screens.item.ProductNetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun refreshLogic(
    offlineMode: Boolean,
    scope: CoroutineScope,
    productUiState: ProductNetworkState,
    offlineProduct: Product,
    getProduct: (Int) -> Unit,
    getOfflineProduct: (Int) -> Unit,
    isRefreshing: (Boolean) -> Unit,
) {
    isRefreshing(true)
    if (!offlineMode) {
        scope.launch {
            delay(1_000L)
            when (productUiState) {
                is ProductNetworkState.Success -> getProduct(productUiState.product.id)
                else -> null
            }
            isRefreshing(false)
        }
    }
    else {
        scope.launch {
            delay(1_000L)
            getOfflineProduct(offlineProduct.id)
            isRefreshing(false)
        }
    }
}