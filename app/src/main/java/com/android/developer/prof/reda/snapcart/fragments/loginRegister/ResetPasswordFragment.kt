package com.android.developer.prof.reda.snapcart.fragments.loginRegister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.developer.prof.reda.snapcart.R
import com.android.developer.prof.reda.snapcart.databinding.FragmentResetPasswordBinding
import com.android.developer.prof.reda.snapcart.util.Resource
import com.android.developer.prof.reda.snapcart.viewModel.ResetPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {
    private lateinit var binding: FragmentResetPasswordBinding
    private val viewModel by viewModels<ResetPasswordViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_reset_password,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arrowBackButton.setOnClickListener {
            findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
        }

        binding.createAccountFromResetTv.setOnClickListener {
            findNavController().navigate(R.id.action_resetPasswordFragment_to_registerFragment)
        }

        binding.passwordResetButton.setOnClickListener {
            viewModel.resetPassword(binding.forgotEmailEt.text.toString())
        }

        lifecycleScope.launch {
            viewModel.resetPassword.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.resetPasswordProgressbar.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        binding.resetPasswordProgressbar.visibility = View.GONE
                        findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
                        Toast.makeText(requireContext(), "Check your email", Toast.LENGTH_LONG).show()
                    }
                    is Resource.Failed ->{
                        binding.resetPasswordProgressbar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Exception ${it.message.toString()}", Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
    }

}