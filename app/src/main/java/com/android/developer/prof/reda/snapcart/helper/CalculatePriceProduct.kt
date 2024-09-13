package com.android.developer.prof.reda.snapcart.helper

fun Double?.getTax(price: Double): Double{
    val percentTax = 0.02

    return (price * (percentTax * 100)).div(100)
}