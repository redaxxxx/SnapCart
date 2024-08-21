package com.android.developer.prof.reda.snapcart.model

data class User(
    val userName: String,
    val email: String,
    val mobileNumber: String
){
    constructor(): this("","","")
}