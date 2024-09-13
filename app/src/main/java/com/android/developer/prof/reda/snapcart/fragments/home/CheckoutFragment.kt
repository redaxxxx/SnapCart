package com.android.developer.prof.reda.snapcart.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.android.developer.prof.reda.snapcart.R
import com.android.developer.prof.reda.snapcart.databinding.FragmentCheckoutBinding
import com.android.developer.prof.reda.snapcart.model.CartProduct

class CheckoutFragment : Fragment() {
    private lateinit var binding: FragmentCheckoutBinding
    private var products = emptyList<CartProduct>()
    private var totalPrice: Double = 0.0
    private var subTotal: Double = 0.0
    private var tax: Double = 0.0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_checkout,
            container,
            false
        )

        products = CheckoutFragmentArgs.fromBundle(requireArguments()).products.toList()
        totalPrice = CheckoutFragmentArgs.fromBundle(requireArguments()).totalPrice.toDouble()
        subTotal = CheckoutFragmentArgs.fromBundle(requireArguments()).subTotal.toDouble()
        tax = CheckoutFragmentArgs.fromBundle(requireArguments()).tax.toDouble()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.forwardAddAddressBtn.setOnClickListener {
            // navigate to add address 
            findNavController().navigate(CheckoutFragmentDirections.actionCheckoutFragmentToShippingAddressFragment())
        }

        binding.apply {
            total.text = "$$totalPrice"
            subtotal.text = "$$subTotal"
            totalTax.text = "$$tax"
        }

        binding.placeOrderBtn.setOnClickListener {
            //place order

        }
    }
}