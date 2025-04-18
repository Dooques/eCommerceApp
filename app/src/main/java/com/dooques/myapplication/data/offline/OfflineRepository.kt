package com.dooques.myapplication.data.offline

import android.content.Context
import com.dooques.myapplication.model.Geolocation
import com.dooques.myapplication.model.Name
import com.dooques.myapplication.model.Product
import com.dooques.myapplication.model.Rating
import com.dooques.myapplication.model.UserAddress
import com.dooques.myapplication.model.UserProfile
import com.dooques.myapplication.util.JsonUtil
import com.dooques.myapplication.util.JsonUtil.OfflineGeolocation
import com.dooques.myapplication.util.JsonUtil.OfflineName
import com.dooques.myapplication.util.JsonUtil.OfflineProduct
import com.dooques.myapplication.util.JsonUtil.OfflineRating
import com.dooques.myapplication.util.JsonUtil.OfflineUserAddress
import com.dooques.myapplication.util.JsonUtil.OfflineUserProfile

interface OfflineRepository {
    fun returnProducts(context: Context): List<Product>
    fun returnUsers(context: Context): List<UserProfile>
}

class OfflineDataRepository(): OfflineRepository {

    override fun returnProducts(context: Context): List<Product> {
        val jsonResult = JsonUtil.getOfflineProductList(context, "products.json")
        val productList = jsonResult?.mapNotNull { it.toProduct() }
        return if (productList.isNullOrEmpty()) emptyList<Product>() else productList
    }

    override fun returnUsers(context: Context): List<UserProfile> {
        val jsonResult = JsonUtil.getOfflineUserProfiles(context, "users.json")
        val userProfileList = jsonResult?.mapNotNull { it.toUserProfile() }
        return if (userProfileList.isNullOrEmpty()) emptyList() else userProfileList
    }

}

fun OfflineProduct.toProduct(): Product {
    return Product(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        image = image,
        rating = rating.toRating()
    )
}

fun OfflineRating.toRating(): Rating {
    return Rating(
        rate = rate,
        count = count
    )
}

fun OfflineUserProfile.toUserProfile() =
    UserProfile(
        id = id,
        username = username,
        email = email,
        password = password,
        name = name.toName(),
        phone = phone,
        address = address.toUserAddress()
    )

fun OfflineName.toName() =
    Name(
        firstName = firstName,
        lastName = lastName
    )

fun OfflineUserAddress.toUserAddress() =
    UserAddress(
        geolocation = geolocation.toGeolocation(),
        city = city,
        street = street,
        number = number,
        zipcode = zipcode
    )

fun OfflineGeolocation.toGeolocation() =
    Geolocation(
        lat = lat,
        long = long
    )