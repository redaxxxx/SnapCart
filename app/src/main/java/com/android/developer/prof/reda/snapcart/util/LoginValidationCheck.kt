package com.android.developer.prof.reda.snapcart.util

import android.util.Patterns

fun validateLoginEmail(email: String): LoginValidation{
    if (email.isEmpty()){
        LoginValidation.Failed("Email Cannot be empty")
    }
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        LoginValidation.Failed("Wrong email format")
    }

    return LoginValidation.Success
}

fun validateLoginPassword(password: String): LoginValidation{
    if (password.isEmpty()){
        LoginValidation.Failed("Password cannot be empty")
    }
    if (password.length < 8){
        LoginValidation.Failed("Password should contains 8 char")
    }
    return LoginValidation.Success
}