package com.android.developer.prof.reda.snapcart.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterValidationCheckKtTest{

    @Test
    fun notEmpty_email_register_test(){
        val result = validateRegisterEmail("redanabil518@gmail.com")
        assertThat(result).isEqualTo(RegisterValidation.Success)
    }
    @Test
    fun notEmpty_username_register_test(){
        val result = validationRegisterUsername("reda nabil")
        assertThat(result).isEqualTo(RegisterValidation.Success)
    }
    @Test
    fun notEmpty_mobileNumber_register_test(){
        val result = validateRegisterMobileNumber("01125572678")
        assertThat(result).isEqualTo(RegisterValidation.Success)
    }
    @Test
    fun notEmpty_password_register_test(){
        val result = validationRegisterUsername("123456789")
        assertThat(result).isEqualTo(RegisterValidation.Success)
    }
}