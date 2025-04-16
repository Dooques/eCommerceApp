package com.dooques.myapplication.data.network

import com.dooques.myapplication.model.Product
import com.dooques.myapplication.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface FakeStoreRepository {
    val productList: Flow<List<Product>>
    suspend fun getProduct(id: Int): Product
    suspend fun postProductDetails(product: Product)
    suspend fun getUserProfiles(): List<UserProfile>
    suspend fun getUserProfile(id: Int): UserProfile
    suspend fun postUserProfile(userProfile: UserProfile)
}

class FakeStoreNetworkRepository(
    private val fakeStoreDataSource: FakeStoreDataSource
): FakeStoreRepository {

    /* Products */
    override val productList: Flow<List<Product>> =
        fakeStoreDataSource.productList
            .map { it }

    override suspend fun getProduct(id: Int): Product =
        fakeStoreDataSource.getProduct(id)

    override suspend fun postProductDetails(product: Product) =
        fakeStoreDataSource.postProductDetails(product)


    /* User Profiles */
    override suspend fun getUserProfiles(): List<UserProfile> =
        fakeStoreDataSource.getUserProfiles()

    override suspend fun getUserProfile(id: Int): UserProfile =
        fakeStoreDataSource.getUserProfile(id)

    override suspend fun postUserProfile(userProfile: UserProfile) =
        fakeStoreDataSource.postUserProfile(userProfile)
}