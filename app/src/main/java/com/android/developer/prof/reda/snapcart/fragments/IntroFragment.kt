package com.android.developer.prof.reda.snapcart.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.developer.prof.reda.snapcart.R
import com.android.developer.prof.reda.snapcart.activity.HomeActivity
import com.android.developer.prof.reda.snapcart.databinding.FragmentIntroBinding

class IntroFragment : Fragment() {
    private lateinit var binding: FragmentIntroBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentIntroBinding.inflate(
           layoutInflater
       )

        binding.startButton.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_registerFragment)
        }

        return binding.root
    }
}