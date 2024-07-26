package com.android.developer.prof.reda.snapcart.util

import android.util.Patterns
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.*

@RunWith(AndroidJUnit4::class)
class LoginValidationCheckKtTest{

    @Test
    fun notEmpty_email_login_return_false(){
        val result = validateLoginEmail("redanabil518@gmail.com")
        assertThat(result).isEqualTo(LoginValidation.Success)
    }
    @Test
    fun notEmpty_password_login_test(){
        val result = validateLoginPassword("123456789")
//        assertThat(result).isNotNull()
//        assertThat(result.toString().length).isGreaterThan(7)
        assertThat(result).isEqualTo(LoginValidation.Success)
    }
}