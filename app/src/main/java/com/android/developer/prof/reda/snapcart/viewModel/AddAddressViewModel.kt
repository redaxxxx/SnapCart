package com.android.developer.prof.reda.snapcart.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.snapcart.model.Address
import com.android.developer.prof.reda.snapcart.util.ADDRESS_COLLECTION
import com.android.developer.prof.reda.snapcart.util.AddNewAddressFailedState
import com.android.developer.prof.reda.snapcart.util.AddressValidation
import com.android.developer.prof.reda.snapcart.util.Resource
import com.android.developer.prof.reda.snapcart.util.USER_COLLECTION
import com.android.developer.prof.reda.snapcart.util.validationAddress
import com.android.developer.prof.reda.snapcart.util.validationCity
import com.android.developer.prof.reda.snapcart.util.validationCountry
import com.android.developer.prof.reda.snapcart.util.validationFullName
import com.android.developer.prof.reda.snapcart.util.validationState
import com.android.developer.prof.reda.snapcart.util.validationZipCode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
@HiltViewModel
class AddAddressViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel(){
    private val _addNewAddress = MutableStateFlow<Resource<Address>>(Resource.Unspecified())
    val addNewAddress: StateFlow<Resource<Address>>
        get() = _addNewAddress

    private val _validation = Channel<AddNewAddressFailedState>()
    val validation = _validation.receiveAsFlow()


    fun addNewAddress(address: Address){
        if (isValidation(address)){
            viewModelScope.launch {
                _addNewAddress.emit(Resource.Loading())
            }

            firestore.collection(USER_COLLECTION).document(auth.uid!!)
                .collection(ADDRESS_COLLECTION).document()
                .set(address)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        _addNewAddress.emit(Resource.Success(address))
                    }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _addNewAddress.emit(Resource.Failed(it.message.toString()))
                    }
                }
        }else{
            val addNewAddressFailedState = AddNewAddressFailedState(
                validationFullName(address.fullName),
                validationAddress(address.address),
                validationCity(address.city),
                validationState(address.state),
                validationZipCode(address.zipCode),
                validationCountry(address.country)
            )

            runBlocking {
                _validation.send(addNewAddressFailedState)
            }
        }
    }

    fun isValidation(newAddress: Address): Boolean{
        val fullname = validationFullName(newAddress.fullName)
        val address = validationAddress(newAddress.address)
        val city = validationCity(newAddress.city)
        val state = validationState(newAddress.state)
        val zipcode = validationZipCode(newAddress.zipCode)
        val country = validationCountry(newAddress.country)

        return fullname is AddressValidation.Success &&
                address is AddressValidation.Success &&
                city is AddressValidation.Success &&
                state is AddressValidation.Success &&
                zipcode is AddressValidation.Success &&
                country is AddressValidation.Success
    }
}