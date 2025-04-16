package com.dooques.myapplication.ui.screens.selling

import androidx.lifecycle.ViewModel
import com.dooques.myapplication.data.network.FakeStoreRepository
import com.dooques.myapplication.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class CreateListingViewModel(
    private val fakeDataRepository: FakeStoreRepository
): ViewModel() {

    private val productDetails = MutableStateFlow<Product>(Product())

    fun updateProductDetails(product: Product) {
        productDetails.update { product }
    }

    suspend fun postProductDetails(product: Product) {
        fakeDataRepository.postProductDetails(product)
    }
}