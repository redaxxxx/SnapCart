package com.android.developer.prof.reda.snapcart.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.developer.prof.reda.snapcart.R
import com.android.developer.prof.reda.snapcart.adapters.CartAdapter
import com.android.developer.prof.reda.snapcart.databinding.FragmentCartBinding
import com.android.developer.prof.reda.snapcart.firebase.FirebaseCommon
import com.android.developer.prof.reda.snapcart.util.Resource
import com.android.developer.prof.reda.snapcart.viewModel.CartViewModel
import com.google.firebase.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
const val CART_TAG = "CartFragment"
@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val cartViewModel by viewModels<CartViewModel>()
    private val adapter by lazy { CartAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_cart,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtnCart.setOnClickListener {
            findNavController().navigateUp()
        }

        //when click on plus btn
        adapter.onClickPlus = {cartProduct ->
            cartViewModel.quantityChanging(cartProduct, FirebaseCommon.QuantityChanging.INCREASE)
        }

        //when click on minus btn
        adapter.onClickMinus = {cartProduct ->
            cartViewModel.quantityChanging(cartProduct, FirebaseCommon.QuantityChanging.DECREASE)
        }

        //fetch cart products from firebase firestore
        lifecycleScope.launch {
            cartViewModel.cartProducts.collectLatest {
                when(it){
                    is Resource.Loading-> {
                        binding.progressBarCart.visibility = View.VISIBLE
                    }
                    is Resource.Success-> {
                        binding.progressBarCart.visibility = View.GONE

                        if (it.data?.isEmpty() == true){
                            showEmptyView()
                        }
                        else{
                            hideEmptyView()
                            adapter.submitList(it.data)
                            binding.viewCartRv.adapter = adapter
                        }
                        adapter.submitList(it.data)
                    }
                    is Resource.Failed -> {
                        binding.progressBarCart.visibility = View.GONE
                        Log.d(CART_TAG, "Fetch product error: ${it.message}")
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            cartViewModel.deleteDialog.collectLatest {
                val alertDialog = AlertDialog.Builder(requireActivity()).apply {
                    setTitle("Delete item from cart")
                    setMessage("Do you want to delete item from your cart?")
                    setNegativeButton("Cancel"){dialog,_->
                        dialog.dismiss()
                    }
                    setPositiveButton("Delete"){dialog,_->
                        cartViewModel.deleteItemFromCart(it)
                        dialog.dismiss()
                    }
                }
                alertDialog.create()
                alertDialog.show()
            }
        }
    }

    //hide empty view when cart is not empty
    private fun hideEmptyView(){
        binding.emptyCartTv.visibility = View.GONE
        binding.emptyBoxImg.visibility = View.GONE

        binding.constraintLayout.visibility = View.VISIBLE
        binding.viewCartRv.visibility = View.VISIBLE
    }

    //show empty view when cart is empty
    private fun showEmptyView(){
        binding.emptyCartTv.visibility = View.VISIBLE
        binding.emptyBoxImg.visibility = View.VISIBLE

        binding.constraintLayout.visibility = View.GONE
        binding.viewCartRv.visibility = View.GONE
    }
}