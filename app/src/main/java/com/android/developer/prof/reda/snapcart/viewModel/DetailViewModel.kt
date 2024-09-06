package com.android.developer.prof.reda.snapcart.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.snapcart.firebase.FirebaseCommon
import com.android.developer.prof.reda.snapcart.model.PopularItemModel
import com.android.developer.prof.reda.snapcart.model.CartProduct
import com.android.developer.prof.reda.snapcart.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel(){

    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.Unspecified())
    val addToCart: StateFlow<Resource<CartProduct>>
        get() = _addToCart

    private val cartCollection = firestore.collection("user").document(auth.uid!!)
        .collection("cart")

    fun addNewProduct(cartProduct: CartProduct){
        firebaseCommon.addNewProduct(cartProduct){addedproduct, exception->
            viewModelScope.launch {
                if (exception != null){
                    _addToCart.emit(Resource.Failed(exception.message.toString()))
                }else{
                    _addToCart.emit(Resource.Success(addedproduct!!))
                }
            }
        }
    }

    private fun increaseQuantity(documentId: String, cartProduct: CartProduct){
        firebaseCommon.increaseQuantityProduct(documentId){_,exception ->
            viewModelScope.launch {
                if (exception != null){
                    _addToCart.emit(Resource.Failed(exception.message.toString()))
                }else{
                    _addToCart.emit(Resource.Success(cartProduct))
                }
            }
        }
    }
}