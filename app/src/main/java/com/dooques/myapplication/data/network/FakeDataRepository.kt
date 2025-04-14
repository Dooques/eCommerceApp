package com.dooques.myapplication.data.network

import com.dooques.myapplication.model.Product
import com.dooques.myapplication.model.ProductList

interface FakeDataRepository {
    suspend fun getProduct(id: Int): Product
    suspend fun getProductList(): List<Product>
}

class FakeDataNetworkRepository(
    private val fakeDataApiService: FakeDataApiService
): FakeDataRepository {

    override suspend fun getProduct(id: Int) = fakeDataApiService.getProduct(id)

    override suspend fun getProductList() = fakeDataApiService.getProductList()
}