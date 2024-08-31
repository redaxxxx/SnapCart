package com.android.developer.prof.reda.snapcart.viewModel

import androidx.lifecycle.ViewModel
import com.android.developer.prof.reda.snapcart.model.CartProduct
import com.android.developer.prof.reda.snapcart.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ViewModel(){
    private val _cartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProducts: StateFlow<Resource<List<CartProduct>>>
        get() = _cartProducts

    init {
        fetchCartProducts()
    }
    private fun fetchCartProducts(){

    }
}