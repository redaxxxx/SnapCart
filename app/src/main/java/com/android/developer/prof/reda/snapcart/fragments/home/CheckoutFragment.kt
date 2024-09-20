package com.android.developer.prof.reda.snapcart.fragments.home

import android.app.AlertDialog
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
import com.android.developer.prof.reda.snapcart.databinding.FragmentCheckoutBinding
import com.android.developer.prof.reda.snapcart.model.Address
import com.android.developer.prof.reda.snapcart.model.CartProduct
import com.android.developer.prof.reda.snapcart.model.Order
import com.android.developer.prof.reda.snapcart.model.OrderStatus
import com.android.developer.prof.reda.snapcart.util.Resource
import com.android.developer.prof.reda.snapcart.viewModel.CheckoutViewModel
import com.android.developer.prof.reda.snapcart.viewModel.SharedViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Random
import java.util.UUID

private const val CHECKOUT_TAG = "CheckoutFragment"
@AndroidEntryPoint
class CheckoutFragment : Fragment() {
    private lateinit var binding: FragmentCheckoutBinding
    private var products = emptyList<CartProduct>()
    private var totalPrice: Double = 0.0
    private var subTotal: Double = 0.0
    private var tax: Double = 0.0
    private val sharedViewModel: SharedViewModel by navGraphViewModels(R.id.home_nav)
    private var address: Address? = null
    private val checkoutViewModel by viewModels<CheckoutViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_checkout,
            container,
            false
        )

        products = CheckoutFragmentArgs.fromBundle(requireArguments()).products.toList()
        totalPrice = String.format("%.1f", CheckoutFragmentArgs.fromBundle(requireArguments()).totalPrice).toDouble()
        subTotal = CheckoutFragmentArgs.fromBundle(requireArguments()).subTotal.toDouble()
        tax = String.format("%.1f", CheckoutFragmentArgs.fromBundle(requireArguments()).tax).toDouble()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.forwardAddAddressBtn.setOnClickListener {
            // navigate to add address 
            findNavController().navigate(CheckoutFragmentDirections.actionCheckoutFragmentToShippingAddressFragment())
        }

        binding.backBtnCart.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.apply {
            total.text = "$${totalPrice+10}"
            subtotal.text = "$$subTotal"
            totalTax.text = "$$tax"
        }

        binding.placeOrderBtn.setOnClickListener {
            //place order
            showOrderConfirmationDialog()
        }

        lifecycleScope.launch {
            sharedViewModel.addressOrder.collectLatest {
                when(it){
                    is Resource.Success ->{
                        if (it.data != null){
                            Log.d(CHECKOUT_TAG, "address: ${it.data}")
                            address = it.data
                            binding.apply {
                                addAddressLayout.visibility = View.GONE
                                shippingAddressLayout.visibility = View.VISIBLE
                                it.data.let { address ->
                                    buyerNameTv.text = address.fullName
                                    shippingAddress1Tv.text = address.address
                                    cityCheckoutTv.text = "${address.city}, "
                                    postalCodeCheckoutTv.text = "${address.zipCode}, "
                                    regionCheckoutTv.text = "${address.state}, "
                                    countryCheckoutTv.text = "${address.country}"
                                }
                            }
                        }else{
                            binding.apply {
                                binding.apply {
                                    addAddressLayout.visibility = View.VISIBLE
                                    shippingAddressLayout.visibility = View.GONE
                                }
                            }
                        }

                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            checkoutViewModel.addOrder.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.progressBarCheckout.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        binding.progressBarCheckout.visibility = View.GONE
                        //make screen for success place order
                        findNavController().navigateUp()

                        Snackbar.make(requireView(), "Your order was Placed", Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.Failed ->{
                        binding.progressBarCheckout.visibility = View.GONE
                        Log.d(CHECKOUT_TAG, "Error: ${it.message}")
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun showOrderConfirmationDialog(){
        val simpleDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = simpleDate.format(Date())

        val dialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Place Order Items")
            setMessage("Do you want to Order your items?")
            setPositiveButton("Yes"){dialog,_ ->
                checkoutViewModel.placeOrder(
                    Order(
                        UUID.randomUUID().toString(),
                        OrderStatus.Ordered.toString(),
                        address,
                        totalPrice,
                        currentDate,
                        products
                    )
                )
                dialog.dismiss()
            }

            setNegativeButton("Cancel"){dialog,_->
                dialog.dismiss()
            }
        }

        dialog.create()
        dialog.show()
    }
}