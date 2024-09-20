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
import androidx.navigation.navGraphViewModels
import com.android.developer.prof.reda.snapcart.R
import com.android.developer.prof.reda.snapcart.adapters.AddressAdapter
import com.android.developer.prof.reda.snapcart.databinding.FragmentShippingAddressBinding
import com.android.developer.prof.reda.snapcart.util.Resource
import com.android.developer.prof.reda.snapcart.viewModel.SharedViewModel
import com.android.developer.prof.reda.snapcart.viewModel.ShippingAddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
private const val SHIPPING_ADDRESS_TAG = "ShippingAddressFragment"
@AndroidEntryPoint
class ShippingAddressFragment : Fragment() {
    private lateinit var binding: FragmentShippingAddressBinding
    private val shippingAddress by viewModels<ShippingAddressViewModel>()
    private val addressAdapter by lazy { AddressAdapter() }
    private val sharedViewModel: SharedViewModel by navGraphViewModels(R.id.home_nav)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        binding.addressViewsRv.adapter = addressAdapter
        binding.addNewAddressBtn.setOnClickListener {
            findNavController().navigate(
                ShippingAddressFragmentDirections.actionShippingAddressFragmentToAddShippingAddressFragment()
            )
        }

        addressAdapter.onCheckboxClick = {
            sharedViewModel.setAddressInfo(it)
            Log.d(SHIPPING_ADDRESS_TAG, "address: $it")
        }

        binding.chooseAddressBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.backBtnShippingAddress.setOnClickListener {
            findNavController().navigateUp()
        }

        lifecycleScope.launch {
            shippingAddress.addresses.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.progressBarShippingAddress.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        binding.progressBarShippingAddress.visibility = View.GONE
                        addressAdapter.submitList(it.data)
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