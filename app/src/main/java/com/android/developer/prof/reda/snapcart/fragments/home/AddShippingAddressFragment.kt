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
import com.android.developer.prof.reda.snapcart.databinding.FragmentAddShippingAddressBinding
import com.android.developer.prof.reda.snapcart.databinding.FragmentShippingAddressBinding
import com.android.developer.prof.reda.snapcart.model.Address
import com.android.developer.prof.reda.snapcart.util.Resource
import com.android.developer.prof.reda.snapcart.viewModel.AddAddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val ADD_ADDRESS_TAG: String = "AddShippingAddressFragment"
@AndroidEntryPoint
class AddShippingAddressFragment : Fragment() {
    private lateinit var binding: FragmentAddShippingAddressBinding
    private val addNewAddressViewModel by viewModels<AddAddressViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_shipping_address,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtnAddAddress.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.apply {
            saveAddressBtn.setOnClickListener {
                addNewAddressViewModel.addNewAddress(
                    Address(
                        fullNameEt.text?.trim().toString(),
                        addressEt.text?.trim().toString(),
                        cityEt.text?.trim().toString(),
                        stateEt.text?.trim().toString(),
                        zipcodeEt.text?.trim().toString(),
                        countryEt.text?.trim().toString()
                    )
                )
            }
        }

        lifecycleScope.launch {
            addNewAddressViewModel.addNewAddress.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.progressBarAddNewAddress.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        binding.progressBarAddNewAddress.visibility = View.GONE

                        findNavController().navigateUp()

                        //clear all edit text
                        binding.apply {
                            fullNameEt.text?.clear()
                            addressEt.text?.clear()
                            cityEt.text?.clear()
                            stateEt.text?.clear()
                            zipcodeEt.text?.clear()
                            countryEt.text?.clear()
                        }
                    }
                    is Resource.Failed ->{
                        binding.progressBarAddNewAddress.visibility = View.GONE

                        Log.d(ADD_ADDRESS_TAG, "Error is ${it.message}")
                    }
                    else -> Unit
                }
            }
        }
    }
}