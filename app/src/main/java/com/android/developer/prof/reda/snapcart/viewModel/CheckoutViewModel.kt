package com.android.developer.prof.reda.snapcart.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.snapcart.model.Address
import com.android.developer.prof.reda.snapcart.model.Order
import com.android.developer.prof.reda.snapcart.util.CART_COLLECTION
import com.android.developer.prof.reda.snapcart.util.ORDER_COLLECTION
import com.android.developer.prof.reda.snapcart.util.Resource
import com.android.developer.prof.reda.snapcart.util.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel(){

    private val _addOrder = MutableStateFlow<Resource<Order>>(Resource.Unspecified())
    val addOrder: StateFlow<Resource<Order>>
        get() = _addOrder
    fun placeOrder(order: Order){
        // place order

        viewModelScope.launch {
            _addOrder.emit(Resource.Loading())
        }

        firestore.runBatch{
            firestore.collection(USER_COLLECTION).document(auth.uid!!)
                .collection(ORDER_COLLECTION).document()
                .set(order)

            firestore.collection(ORDER_COLLECTION).document().set(order)

            firestore.collection(USER_COLLECTION).document(auth.uid!!)
                .collection(CART_COLLECTION).get()
                .addOnSuccessListener {
                    it.documents.forEach {documentSnapshot->
                        documentSnapshot.reference.delete()
                    }
                }
        }.addOnSuccessListener {
            viewModelScope.launch {
                _addOrder.emit(Resource.Success(order))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _addOrder.emit(Resource.Failed(it.message.toString()))
            }
        }


    }
}