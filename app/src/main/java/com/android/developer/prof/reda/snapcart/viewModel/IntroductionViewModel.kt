package com.android.developer.prof.reda.snapcart.viewModel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.snapcart.util.INTRODUCTION_FRAGMENT
import com.android.developer.prof.reda.snapcart.util.INTRODUCTION_KEY
import com.android.developer.prof.reda.snapcart.util.SHOPPING_ACTIVITY
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val sharedPreferences: SharedPreferences
) : ViewModel(){

    private val _navigate = MutableStateFlow(0)
    val navigate: StateFlow<Int>
        get() = _navigate

    init {
        val isButtonClick = sharedPreferences.getBoolean(INTRODUCTION_KEY, false)
        val user = firebaseAuth.currentUser

        if (user != null){
            viewModelScope.launch {
                _navigate.emit(SHOPPING_ACTIVITY)
            }
        }else if(isButtonClick) {
            viewModelScope.launch {
                _navigate.emit(INTRODUCTION_FRAGMENT)
            }
        }
    }

    fun startClickButton(){
        sharedPreferences.edit().putBoolean(INTRODUCTION_KEY, true).apply()
    }
}