package com.android.developer.prof.reda.snapcart.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.developer.prof.reda.snapcart.R
import com.android.developer.prof.reda.snapcart.activity.HomeActivity
import com.android.developer.prof.reda.snapcart.databinding.FragmentLoginBinding
import com.android.developer.prof.reda.snapcart.util.Resource
import com.android.developer.prof.reda.snapcart.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(
            layoutInflater
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

        lifecycleScope.launch {
            viewModel.login.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        binding.loginProgressbar.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        startActivity(Intent(requireActivity(), HomeActivity::class.java))
                        binding.loginProgressbar.visibility = View.GONE
                    }
                    is Resource.Failed ->{
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG).show()
                        binding.loginProgressbar.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }

    }

}