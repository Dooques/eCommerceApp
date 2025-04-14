package com.dooques.myapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val title: String,
    val price: Float,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating
)

@Serializable
data class ProductList(
    val totalItems: Int,
    val productList: List<Product>
)

@Serializable
data class Rating (
    val rate: Float,
    val count: Int
)


sealed interface ProductListUiState {
    data class Success(val products: List<Product>): ProductListUiState
    data class Error(val e: Exception): ProductListUiState
    object Loading: ProductListUiState
}