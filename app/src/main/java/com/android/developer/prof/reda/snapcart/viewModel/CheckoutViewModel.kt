package com.android.developer.prof.reda.snapcart.viewModel

import androidx.lifecycle.ViewModel
import com.android.developer.prof.reda.snapcart.model.Address
import com.android.developer.prof.reda.snapcart.model.Order
import com.android.developer.prof.reda.snapcart.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class CheckoutViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel(){

    private val _addOrder = MutableStateFlow<Resource<Order>>(Resource.Unspecified())
    val addOrder: StateFlow<Resource<Order>>
        get() = _addOrder
    fun placeOrder(){
        // place order
    }
}