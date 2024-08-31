package com.android.developer.prof.reda.snapcart.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartProduct(
    val popularItemModel: PopularItemModel,
    val quantity: Int,
    val colorSelected: String? = null,
    val sizeSelected: String? = null
): Parcelable{
    constructor(): this(PopularItemModel(), 1,null, null)
}