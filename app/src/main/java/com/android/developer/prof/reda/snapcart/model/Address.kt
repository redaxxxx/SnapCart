package com.android.developer.prof.reda.snapcart.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val fullName: String,
    val address: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val country: String
): Parcelable{
    constructor(): this("", "", "","", "", "")
}
