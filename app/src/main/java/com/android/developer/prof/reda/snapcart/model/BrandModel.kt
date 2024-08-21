package com.android.developer.prof.reda.snapcart.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BrandModel(
    val id: Int,
    val picUrl: String,
    val title: String
): Parcelable{
    constructor(): this(0,"","")
}