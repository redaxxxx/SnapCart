package com.android.developer.prof.reda.snapcart.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PopularItemModel(
    //fix empty list
    val picUrl: List<String>,
    val price: Double,
    val rating: Double,
    val size: List<String>,
    val title: String,
    val numberInCart: Int,
    ): Parcelable{
    constructor() : this(picUrl = emptyList(),0.0,0.0, size = emptyList(), "", 0)
}