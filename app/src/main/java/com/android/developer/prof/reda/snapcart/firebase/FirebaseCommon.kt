package com.android.developer.prof.reda.snapcart.firebase

import com.android.developer.prof.reda.snapcart.model.CartProduct
import com.android.developer.prof.reda.snapcart.util.CART_COLLECTION
import com.android.developer.prof.reda.snapcart.util.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseCommon(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) {
    private val cartCollection = firestore.collection(USER_COLLECTION).document(auth.uid!!)
        .collection(CART_COLLECTION)


    fun addNewProduct(cartProduct: CartProduct, onResult: (CartProduct?, Exception?) -> Unit){
        cartCollection.document().set(cartProduct)
            .addOnFailureListener {
                onResult(null, it)
            }.addOnSuccessListener {
                onResult(cartProduct, null)
            }
    }
    fun increaseQuantityProduct(documentId: String, onResult: (String?, Exception?)-> Unit){
        firestore.runTransaction {transition->
            val documentRef = cartCollection.document(documentId)
            val document = transition.get(documentRef)
            val productObject = document.toObject(CartProduct::class.java)

            productObject?.let {cartProduct ->
                val newQuantity = cartProduct.quantity + 1
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transition.set(documentRef, newProductObject)
            }
        }.addOnSuccessListener {
            onResult(documentId, null)
        }.addOnFailureListener {
            onResult(null, it)
        }
    }

    fun decreaseQuantityProduct(documentId: String, onResult: (String?, Exception?) -> Unit){
        firestore.runTransaction {transition->
            val documentRef = cartCollection.document(documentId)
            val document = transition.get(documentRef)
            val productObject = document.toObject(CartProduct::class.java)

            productObject?.let {cartProduct ->
                val newQuantity = cartProduct.quantity - 1
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transition.set(documentRef, newProductObject)
            }
        }.addOnSuccessListener {
            onResult(documentId, null)
        }.addOnFailureListener {
            onResult(null, it)
        }
    }

    enum class QuantityChanging{
        INCREASE, DECREASE
    }
}