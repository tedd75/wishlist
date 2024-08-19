package com.example.wishlist.model

import androidx.annotation.DrawableRes

data class Products(
    val id: Long,
    val name: String,
    val address: List<String>,
    @DrawableRes
    val resId: Int
)

