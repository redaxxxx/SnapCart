package com.android.developer.prof.reda.snapcart.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.developer.prof.reda.snapcart.R
import com.android.developer.prof.reda.snapcart.databinding.FragmentShippingAddressBinding
import com.android.developer.prof.reda.snapcart.util.Resource
import com.android.developer.prof.reda.snapcart.viewModel.ShippingAddressViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
private const val SHIPPING_ADDRESS_TAG = "ShippingAddressFragment"
class ShippingAddressFragment : Fragment() {
    private lateinit var binding: FragmentShippingAddressBinding
    private val shippingAddress by viewModels<ShippingAddressViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_shipping_address,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addNewAddressBtn.setOnClickListener {
            findNavController().navigate(
                ShippingAddressFragmentDirections.actionShippingAddressFragmentToAddShippingAddressFragment()
            )
        }

        lifecycleScope.launch {
            shippingAddress.addresses.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.progressBarShippingAddress.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        binding.progressBarShippingAddress.visibility = View.GONE

                    }
                    is Resource.Failed ->{
                        binding.progressBarShippingAddress.visibility = View.GONE
                        Log.d(SHIPPING_ADDRESS_TAG, "Error is: ${it.message}")
                    }
                    else -> Unit
                }
            }
        }
    }
}