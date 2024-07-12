package com.android.developer.prof.reda.snapcart.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.snapcart.util.LoginFailedState
import com.android.developer.prof.reda.snapcart.util.LoginValidation
import com.android.developer.prof.reda.snapcart.util.Resource
import com.android.developer.prof.reda.snapcart.util.validateLoginEmail
import com.android.developer.prof.reda.snapcart.util.validateLoginPassword

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth
): ViewModel() {

    private val _login = MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    val login: StateFlow<Resource<FirebaseUser>>
        get() = _login

    private val _validation = Channel<LoginFailedState>()
    val validation = _validation.receiveAsFlow()

    fun loginUser(email: String, password: String){
        if (checkValidation(email, password)){

            runBlocking {
                _login.emit(Resource.Loading())
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnFailureListener {
                    viewModelScope.launch {
                        _login.emit(Resource.Failed(it.message.toString()))
                    }
                }
                .addOnSuccessListener {
                    viewModelScope.launch {
                        _login.emit(Resource.Success(it.user!!))
                    }
                }
        }else{
            val loginFailedState = LoginFailedState(
                validateLoginEmail(email),
                validateLoginPassword(password)
            )
            runBlocking {
                _validation.send(loginFailedState)
            }
        }
    }

    private fun checkValidation(email: String, password: String): Boolean{
        val emailValidation = validateLoginEmail(email)
        val passwordValidation = validateLoginPassword(password)

        return emailValidation is LoginValidation.Success &&
                passwordValidation is LoginValidation.Success
    }
}