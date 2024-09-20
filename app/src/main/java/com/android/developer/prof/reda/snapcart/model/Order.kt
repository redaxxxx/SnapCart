package com.android.developer.prof.reda.snapcart.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val id: String,
    val orderStatus: String,
    val shippingAddress: Address?,
    val totalPrice: Double,
    val date: String,
    val products: List<CartProduct>? = emptyList()
): Parcelable{
    constructor(): this("", "", null, 0.0,"")
}