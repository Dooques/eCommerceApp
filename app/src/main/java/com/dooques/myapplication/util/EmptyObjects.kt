package com.dooques.myapplication.util

import com.dooques.myapplication.model.Product
import com.dooques.myapplication.model.Rating

val emptyProduct = Product(
    id = 0,
    title = "",
    price = 0f,
    description = "",
    category = "",
    image = "",
    rating = Rating(0f, 0)
)