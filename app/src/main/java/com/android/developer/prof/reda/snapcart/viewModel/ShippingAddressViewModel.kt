package com.android.developer.prof.reda.snapcart.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.snapcart.model.Address
import com.android.developer.prof.reda.snapcart.util.ADDRESS_COLLECTION
import com.android.developer.prof.reda.snapcart.util.Resource
import com.android.developer.prof.reda.snapcart.util.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ShippingAddressViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel(){

    private val _addresses = MutableStateFlow<Resource<List<Address>>>(Resource.Unspecified())
    val addresses: StateFlow<Resource<List<Address>>>
        get() = _addresses

    var addressesDocument = emptyList<DocumentSnapshot>()

    init {
        fetchAddresses()
    }
    private fun fetchAddresses(){
        viewModelScope.launch {
            _addresses.emit(Resource.Loading())
        }

        firestore.collection(USER_COLLECTION).document(auth.uid!!)
            .collection(ADDRESS_COLLECTION)
            .addSnapshotListener{value, error ->
                if (error != null && value == null){
                    viewModelScope.launch {
                        _addresses.emit(Resource.Failed(error.message.toString()))
                    }
                }else{
                    viewModelScope.launch {
                        if (value != null){
                            addressesDocument = value.documents
                        }
                        _addresses.emit(Resource.Success(value!!.toObjects(Address::class.java)))
                    }
                }
            }
    }
}