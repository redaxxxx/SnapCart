package com.android.developer.prof.reda.snapcart.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.snapcart.model.Address
import com.android.developer.prof.reda.snapcart.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    private val _addressOrder = MutableStateFlow<Resource<Address>>(Resource.Unspecified())
    val addressOrder: StateFlow<Resource<Address>>
        get() = _addressOrder

    fun setAddressInfo(address: Address){
        viewModelScope.launch {
            _addressOrder.emit(Resource.Success(address))
        }
    }
}