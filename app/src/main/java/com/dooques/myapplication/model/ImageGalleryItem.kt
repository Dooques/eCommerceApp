package com.dooques.myapplication.model

import java.util.UUID

data class ImageGalleryItem(
    val imageId: String = UUID.randomUUID().toString(),
    val title: String,
    var selected: Boolean = false,
    val image: Int,
)
