package com.android.developer.prof.reda.snapcart.util

import android.util.Patterns

fun validationRegisterUsername(firstName: String): RegisterValidation{
    if (firstName.isEmpty()){
        RegisterValidation.Failed("First Name cannot be empty")
    }

    return RegisterValidation.Success
}

fun validateRegisterEmail(email: String): RegisterValidation{
    if (email.isEmpty()){
        RegisterValidation.Failed("Email Cannot be empty")
    }
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        RegisterValidation.Failed("Wrong email format")
    }
    return RegisterValidation.Success
}

fun validateRegisterMobileNumber(mobileNumber: String): RegisterValidation{
    if (mobileNumber.isEmpty()){
        RegisterValidation.Failed("Mobile number cannot be empty")
    }
    if (!Patterns.PHONE.matcher(mobileNumber).matches()){
        RegisterValidation.Failed("Wrong mobile number format")
    }
    return RegisterValidation.Success
}

fun validateRegisterPassword(password: String): RegisterValidation{
    if (password.isEmpty()){
        RegisterValidation.Failed("Password cannot be empty")
    }
    if (password.length < 6){
        RegisterValidation.Failed("Password should contains 6 char")
    }

    return RegisterValidation.Success
}