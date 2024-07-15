package com.android.developer.prof.reda.snapcart.data

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val mobileNumber: String
){
    constructor(): this("","","","")
}