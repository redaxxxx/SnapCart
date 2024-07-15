package com.android.developer.prof.reda.snapcart.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.developer.prof.reda.snapcart.R
import com.android.developer.prof.reda.snapcart.data.User
import com.android.developer.prof.reda.snapcart.databinding.FragmentRegisterBinding
import com.android.developer.prof.reda.snapcart.util.RegisterValidation
import com.android.developer.prof.reda.snapcart.util.Resource
import com.android.developer.prof.reda.snapcart.viewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val REGISTER_TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(
            inflater,
            container,
            false
        )


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createAccountTv.setOnClickListener {
            val user = User(
                binding.firstNameEt.text.toString(),
                binding.lastNameEt.text.toString(),
                binding.emailRegisterEt.text.toString(),
                binding.mobileNumberEt.text.toString()
            )
            val password = binding.passwordRegisterEt.text.toString()

            viewModel.createNewAccount(user, password)
        }

        lifecycleScope.launch {
            viewModel.register.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.singUpProgressbar.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        binding.singUpProgressbar.visibility = View.GONE
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                    is Resource.Failed ->{
                        binding.singUpProgressbar.visibility = View.GONE
                        Log.d(REGISTER_TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.validation.collectLatest {
                if (it.firstName is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.firstNameEt.apply {
                            requestFocus()
                            error = it.firstName.message
                        }
                    }
                }
            }
            viewModel.validation.collectLatest {
                if (it.lastName is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.lastNameEt.apply {
                            requestFocus()
                            error = it.lastName.message
                        }
                    }
                }
            }
            viewModel.validation.collectLatest {
                if (it.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.emailRegisterEt.apply {
                            requestFocus()
                            error = it.email.message
                        }
                    }
                }
            }
            viewModel.validation.collectLatest {
                if (it.mobileNumber is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.mobileNumberEt.apply {
                            requestFocus()
                            error = it.mobileNumber.message
                        }
                    }
                }
            }
            viewModel.validation.collectLatest {
                if (it.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.passwordRegisterEt.apply {
                            requestFocus()
                            error = it.password.message
                        }
                    }
                }
            }
        }
    }

}