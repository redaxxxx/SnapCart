package com.android.developer.prof.reda.snapcart.model

sealed class OrderStatus (val status: String){
    object Ordered: OrderStatus("Ordered")
    object Canceled: OrderStatus("Canceled")
    object Confirmed: OrderStatus("Confirmed")
    object Shipped: OrderStatus("Shipped")
    object Delivered: OrderStatus("Delivered")
    object Returned: OrderStatus("Returned")
}