package com.dooques.myapplication.util

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.serialization.SerialName

object JsonUtil {
    val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    fun getOfflineProductList(context: Context, file: String): List<OfflineProduct>? {

        val listType = Types.newParameterizedType(List::class.java, OfflineProduct::class.java)
        val adapter: JsonAdapter<List<OfflineProduct>> = moshi.adapter(listType)
        val myJSON = context.assets.open(file).bufferedReader().use { it.readText() }

        return adapter.fromJson(myJSON)
    }

    fun getOfflineUserProfiles(context: Context, file: String): List<OfflineUserProfile>? {

        val listType = Types.newParameterizedType(List::class.java, OfflineUserProfile::class.java)
        val adapter: JsonAdapter<List<OfflineUserProfile>> = moshi.adapter(listType)
        val myJSON = context.assets.open(file).bufferedReader().use { it.readText() }

        return adapter.fromJson(myJSON)
    }

    @JsonClass(generateAdapter = true)
    data class OfflineProduct(
        val id: Int = 0,
        val title: String = "",
        val price: Float = 0.00f,
        val description: String = "",
        val category: String = "",
        val image: String = "",
        val rating: OfflineRating = OfflineRating()
    )
    @JsonClass(generateAdapter = true)
    data class OfflineRating (
        val rate: Float = 0f,
        val count: Int = 0
    )

    @JsonClass(generateAdapter = true)
    data class OfflineUserProfile(
        val id: Int = 0,
        val username: String = "",
        val email: String = "",
        val password: String = "",
        val name: OfflineName = OfflineName(),
        val phone: String = "",
        val address: OfflineUserAddress = OfflineUserAddress()
    )

    @JsonClass(generateAdapter = true)
    data class OfflineUserAddress(
        val geolocation: OfflineGeolocation = OfflineGeolocation(),
        val city: String = "",
        val street: String = "",
        val number: Int = 0,
        val zipcode: String = ""
    )

    @JsonClass(generateAdapter = true)
    data class OfflineName(
        @SerialName("firstname") val firstName: String = "",
        @SerialName("lastname") val lastName: String = ""
    )

    @JsonClass(generateAdapter = true)
    data class OfflineGeolocation(
        val lat: String = "",
        val long: String = ""
    )
}