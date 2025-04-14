package com.dooques.myapplication.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dooques.myapplication.data.network.FakeDataRepository
import com.dooques.myapplication.model.ProductListUiState
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(
    private val fakeDataApiRepository: FakeDataRepository
): ViewModel() {

    var productListUiState: ProductListUiState by mutableStateOf(ProductListUiState.Loading)
        private set

    init {
        getFakeData()
    }
    private fun getFakeData() {
        viewModelScope.launch {
             productListUiState = try {
                val productList = fakeDataApiRepository.getProductList()
                ProductListUiState.Success(productList)
            } catch (e: IOException) {
                ProductListUiState.Error(e)
            }
        }
    }
}