package com.dooques.myapplication.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class UserProfile(
    val id: Int = 0,
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val name: Name = Name(),
    val phone: String = "",
    val address: UserAddress = UserAddress()
)

@Serializable
data class UserAddress(
    val geolocation: Geolocation = Geolocation(),
    val city: String = "",
    val street: String = "",
    val number: Int = 0,
    val zipcode: String = ""
)

@Serializable
data class Name(
    @SerialName("firstname") val firstName: String = "",
    @SerialName("lastname") val lastName: String = ""
)

@Serializable
data class Geolocation(
    val lat: String = "",
    val long: String = ""
)

@Serializable
data class UserLoginAuth(
    val username: String,
    val password: String
)

data class UserProfileDetails(
    val username: String = "",
    val email: String = ""
)

@Serializable
data class AuthToken(
    val token: String = ""
)