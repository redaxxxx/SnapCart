package com.android.developer.prof.reda.snapcart.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.snapcart.firebase.FirebaseCommon
import com.android.developer.prof.reda.snapcart.model.CartProduct
import com.android.developer.prof.reda.snapcart.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
): ViewModel(){
    private val _cartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProducts: StateFlow<Resource<List<CartProduct>>>
        get() = _cartProducts

    private val _deleteDialog = MutableSharedFlow<CartProduct>()
    val deleteDialog: SharedFlow<CartProduct>
        get() = _deleteDialog

    private var cartProductDocument = emptyList<DocumentSnapshot>()


    init {
        fetchCartProducts()
    }
    private fun fetchCartProducts(){
        viewModelScope.launch {
            _cartProducts.emit(Resource.Loading())
        }
        firestore.collection("user").document(auth.uid!!)
            .collection("cart")
            .addSnapshotListener{value, error->
                if (error != null && value == null){
                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Failed(error.message.toString()))
                    }
                }else{
                    viewModelScope.launch {
                        if (value != null){
                            cartProductDocument = value.documents
                        }
                        _cartProducts.emit(Resource.Success(value!!.toObjects(CartProduct::class.java)))
                    }
                }
            }
    }

    fun deleteItemFromCart(cartProduct: CartProduct){
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if (index != null && index != -1){
            val documentId = cartProductDocument[index].id

            firestore.collection("user").document(auth.uid!!)
                .collection("cart").document(documentId).delete()
        }
    }
    fun quantityChanging(
        cartProduct: CartProduct,
        quantityChanging: FirebaseCommon.QuantityChanging
    ){
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if (index != null && index != -1){
            val documentId = cartProductDocument[index].id

            when(quantityChanging){

                //when increase quantity
                FirebaseCommon.QuantityChanging.INCREASE ->{
                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Loading())
                    }
                    increaseQuantity(documentId)
                }

                //when decrease quantity
                FirebaseCommon.QuantityChanging.DECREASE ->{
                    if (cartProduct.quantity == 1){
                        //deleted cart item
                        viewModelScope.launch {
                            _deleteDialog.emit(cartProduct)
                        }
                        return
                    }
                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Loading())
                    }
                    decreaseQuantity(documentId)
                }
            }
        }
    }

    private fun increaseQuantity(documentId: String){
        firebaseCommon.increaseQuantityProduct(documentId){_, e->
            if (e != null){
                viewModelScope.launch {
                    _cartProducts.emit(Resource.Failed(e.message.toString()))
                }
            }
        }
    }

    private fun decreaseQuantity(documentId: String){
        firebaseCommon.decreaseQuantityProduct(documentId){_, e->
            if (e != null){
                viewModelScope.launch {
                    _cartProducts.emit(Resource.Failed(e.message.toString()))
                }
            }
        }
    }


}