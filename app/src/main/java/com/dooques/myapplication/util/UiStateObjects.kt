package com.dooques.myapplication.util

import com.dooques.myapplication.model.ImageGalleryItem
import com.dooques.myapplication.model.Product
import com.dooques.myapplication.ui.screens.home.ProductListNetworkState

data class ImageGalleryUiState(
    val imageGalleryItem: ImageGalleryItem = ImageGalleryItem()
)

data class ProductListUiState(
    val productList: List<Product> = listOf()
)

data class ProductUiState(
    val product: Product = Product()
)