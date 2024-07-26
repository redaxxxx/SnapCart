package com.android.developer.prof.reda.snapcart.util

sealed class RegisterValidation{
    object Success: RegisterValidation()
    data class Failed(val message: String): RegisterValidation()
}
data class RegisterFailedState(
    val username: RegisterValidation,
    val email: RegisterValidation,
    val mobileNumber: RegisterValidation,
    val password: RegisterValidation
)