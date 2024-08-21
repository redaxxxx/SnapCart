package com.android.developer.prof.reda.snapcart.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.snapcart.model.User
import com.android.developer.prof.reda.snapcart.util.RegisterFailedState
import com.android.developer.prof.reda.snapcart.util.RegisterValidation
import com.android.developer.prof.reda.snapcart.util.Resource
import com.android.developer.prof.reda.snapcart.util.USER_COLLECTION
import com.android.developer.prof.reda.snapcart.util.validateRegisterEmail
import com.android.developer.prof.reda.snapcart.util.validateRegisterMobileNumber
import com.android.developer.prof.reda.snapcart.util.validateRegisterPassword
import com.android.developer.prof.reda.snapcart.util.validationRegisterUsername
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

const val TAG = "RegisterViewModel"
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
): ViewModel(){


    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: StateFlow<Resource<User>>
        get() = _register

    private val _validation = Channel<RegisterFailedState>()
    val validation = _validation.receiveAsFlow()
    fun createNewAccount(user: User, password: String){
        if (checkValidation(user, password)){
            auth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {result->
                    result.user?.let {
                        saveUser(it.uid, user)
                    }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _register.emit(Resource.Failed(it.message.toString()))
                    }
                }
        }else{
            val registerFailedState = RegisterFailedState(
                validationRegisterUsername(user.userName),
                validateRegisterEmail(user.email),
                validateRegisterMobileNumber(user.mobileNumber),
                validateRegisterPassword(password)
            )

            runBlocking {
                _validation.send(registerFailedState)
            }
        }
    }

    private fun saveUser(userId: String, user: User){
        db.collection(USER_COLLECTION)
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }.addOnFailureListener {
                Log.d(TAG, "Exception: ${it.message.toString()}")
                _register.value = Resource.Failed(it.message.toString())

            }
    }
    private fun checkValidation(user: User , password: String): Boolean{
        val firstNameValidation = validationRegisterUsername(user.userName)
        val emailValidation = validateRegisterEmail(user.email)
        val mobileNumberValidation = validateRegisterMobileNumber(user.mobileNumber)
        val passwordValidation = validateRegisterPassword(password)

        return firstNameValidation is RegisterValidation.Success &&
                emailValidation is RegisterValidation.Success &&
                mobileNumberValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success
    }
}