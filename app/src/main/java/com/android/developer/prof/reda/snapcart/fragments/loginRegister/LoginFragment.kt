package com.android.developer.prof.reda.snapcart.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.developer.prof.reda.snapcart.R
import com.android.developer.prof.reda.snapcart.activity.HomeActivity
import com.android.developer.prof.reda.snapcart.databinding.FragmentLoginBinding
import com.android.developer.prof.reda.snapcart.util.LoginValidation
import com.android.developer.prof.reda.snapcart.util.Resource
import com.android.developer.prof.reda.snapcart.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val LOGIN_TAG = "LoginFragment"
@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener{
            val email: String = binding.emailEt.text.toString()
            val password: String = binding.passwordEt.text.toString()

            viewModel.loginUser(email, password)
        }

        binding.createAccountLoginTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.forgotPasswordLoginTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }

        lifecycleScope.launch {
            viewModel.login.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        binding.loginProgressbar.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        startActivity(Intent(requireActivity(), HomeActivity::class.java))
                        requireActivity().finish()
                        binding.loginProgressbar.visibility = View.GONE
                    }
                    is Resource.Failed ->{
                        Log.d(LOGIN_TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG).show()
                        binding.loginProgressbar.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.validation.collectLatest {
                if (it.email is LoginValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.emailEt.apply {
                            requestFocus()
                            error = it.email.message
                        }
                    }
                }
                if (it.password is LoginValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.passwordEt.apply {
                            requestFocus()
                            error = it.password.message
                        }
                    }
                }
            }
        }
    }

}