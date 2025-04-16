package com.dooques.myapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: Int = 0,
    val username: String = "",
    val email: String = "",
    val password: String = ""
)