package com.dooques.myapplication.data.network

import com.dooques.myapplication.model.Product
import com.dooques.myapplication.model.ProductList
import retrofit2.http.GET
import retrofit2.http.Path

interface FakeDataApiService {
    @GET("products")
    suspend fun getProductList(): List<Product>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Product
}

