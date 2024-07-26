package com.android.developer.prof.reda.snapcart.activity

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.android.developer.prof.reda.snapcart.R
import com.android.developer.prof.reda.snapcart.databinding.ActivityLoginRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginRegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(
           this,
           R.layout.activity_login_register
       )
    }
}