package com.android.developer.prof.reda.snapcart.fragments.home

import android.content.Intent
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
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.android.developer.prof.reda.snapcart.R
import com.android.developer.prof.reda.snapcart.adapters.BrandAdapter
import com.android.developer.prof.reda.snapcart.adapters.PopularItemAdapter
import com.android.developer.prof.reda.snapcart.adapters.SliderAdapter
import com.android.developer.prof.reda.snapcart.databinding.FragmentExploreBinding
import com.android.developer.prof.reda.snapcart.util.Resource
import com.android.developer.prof.reda.snapcart.viewModel.ExploreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
const val EXPLORE_TAG= "ExploreFragment"
@AndroidEntryPoint
class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding
    private val viewModel by viewModels<ExploreViewModel>()
    private val sliderAdapter by lazy { SliderAdapter() }
    private val brandAdapter by lazy { BrandAdapter() }
    private val popularItemAdapter by lazy { PopularItemAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_explore,
            container,
            false
        )

        popularItemAdapter.onItemClick = {item->
            findNavController().navigate(ExploreFragmentDirections.actionExploreFragmentToDetailFragment(item))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        banners()

        binding.viewBrandRv.adapter = brandAdapter

        binding.viewPopularRv.adapter = popularItemAdapter

        lifecycleScope.launch {
            viewModel.popularItems.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.progressBarViewPopular.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        Log.d(EXPLORE_TAG, it.data.toString())
                        binding.progressBarViewPopular.visibility = View.GONE
                        popularItemAdapter.submitList(it.data)
                    }
                    is Resource.Failed ->{
                        binding.progressBarViewPopular.visibility = View.GONE
                        Log.d(EXPLORE_TAG, "Error when loading popular: ${it.message}")
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.banners.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        binding.progressBarBanner.visibility = View.VISIBLE
                    }
                    is Resource.Success-> {
                        binding.progressBarBanner.visibility = View.GONE
                        sliderAdapter.submitList(it.data)
                    }
                    is Resource.Failed -> {
                        binding.progressBarBanner.visibility = View.GONE
                        Log.d(EXPLORE_TAG, "Error: ${it.message}")
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.brands.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.progressBarViewBrand.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        Log.d(EXPLORE_TAG, it.data.toString())
                        binding.progressBarViewBrand.visibility = View.GONE
                        brandAdapter.submitList(it.data)

                    }
                    is Resource.Failed ->{
                        binding.progressBarViewBrand.visibility = View.GONE
                        Log.d(EXPLORE_TAG, "Error ${it.message}")
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun banners(){
        binding.viewpagerSlider.apply {
            adapter = sliderAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3

            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            val compositePageTransformer = CompositePageTransformer().apply {
                addTransformer(MarginPageTransformer(40))
            }

            setPageTransformer(compositePageTransformer)

        }
    }
}