package com.android.developer.prof.reda.snapcart.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.developer.prof.reda.snapcart.R
import com.android.developer.prof.reda.snapcart.databinding.ActivityHomeBinding
import com.android.developer.prof.reda.snapcart.util.Resource
import com.android.developer.prof.reda.snapcart.viewModel.CartViewModel
import com.android.developer.prof.reda.snapcart.viewModel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val cartViewModel by viewModels<CartViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        val navController by lazy {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment

            navHostFragment.navController
        }

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{_,destination,_ ->
            if (destination.id == R.id.detailFragment || destination.id == R.id.checkoutFragment ||
                destination.id == R.id.shippingAddressFragment || destination.id == R.id.addShippingAddressFragment||
                destination.id == R.id.cartFragment){
                binding.bottomNavigationView.visibility = View.GONE
            }
            else{
                binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }

        // view badge  cart icon for number of products in cart
        lifecycleScope.launch {
            cartViewModel.cartProducts.collectLatest {
                when(it){
                    is Resource.Success ->{
                        val count = it.data?.size
                        if (count != null){
                            binding.bottomNavigationView.getOrCreateBadge(R.id.cartFragment).apply {
                                number = count
                                backgroundColor = resources.getColor(R.color.white)
                            }
                        }
                    }
                    else -> Unit
                }
            }
        }
    }
}