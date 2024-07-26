package com.android.developer.prof.reda.snapcart.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.developer.prof.reda.snapcart.R
import com.android.developer.prof.reda.snapcart.activity.HomeActivity
import com.android.developer.prof.reda.snapcart.databinding.FragmentIntroBinding
import com.android.developer.prof.reda.snapcart.viewModel.IntroductionViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.android.developer.prof.reda.snapcart.util.SHOPPING_ACTIVITY
import com.android.developer.prof.reda.snapcart.util.INTRODUCTION_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroFragment : Fragment() {
    private lateinit var binding: FragmentIntroBinding
    private val viewModel by viewModels<IntroductionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = DataBindingUtil.inflate(
           inflater,
           R.layout.fragment_intro,
           container,
           false
       )

//        binding.startButton.setOnClickListener {
//            findNavController().navigate(R.id.action_introFragment_to_registerFragment)
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.navigate.collectLatest {
                when(it){
                    SHOPPING_ACTIVITY -> {
                        Intent(requireActivity(), HomeActivity::class.java).also {intent->
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        }
                    }

                    INTRODUCTION_FRAGMENT ->{
                        findNavController().navigate(R.id.action_introFragment_to_registerFragment)
                    }
                }
            }
        }
    }
}