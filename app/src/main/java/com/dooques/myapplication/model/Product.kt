package com.dooques.myapplication.model

import android.icu.text.DecimalFormat
import com.dooques.myapplication.util.ProductListUiState
import com.dooques.myapplication.util.ProductUiState
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int = 0,
    val title: String = "",
    val price: Float = 0.00f,
    val description: String = "",
    val category: String = "",
    val image: String = "",
    val rating: Rating = Rating()
)

@Serializable
data class ProductList(
    val totalItems: Int,
    val productList: List<Product>
)

@Serializable
data class Rating (
    val rate: Float = 0f,
    val count: Int = 0
)