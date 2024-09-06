package com.android.developer.prof.reda.snapcart.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.android.developer.prof.reda.snapcart.R
import com.android.developer.prof.reda.snapcart.adapters.ColorAdapter
import com.android.developer.prof.reda.snapcart.adapters.SizeAdapter
import com.android.developer.prof.reda.snapcart.adapters.SliderAdapter
import com.android.developer.prof.reda.snapcart.databinding.FragmentDetailBinding
import com.android.developer.prof.reda.snapcart.model.CartProduct
import com.android.developer.prof.reda.snapcart.model.PopularItemModel
import com.android.developer.prof.reda.snapcart.model.SliderModel
import com.android.developer.prof.reda.snapcart.util.Resource
import com.android.developer.prof.reda.snapcart.viewModel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val DETAIL_TAG = "DetailFragment"
@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()
    private val colorListAdapter by lazy { ColorAdapter() }
    private val sizeListAdapter by lazy { SizeAdapter() }
    private val sliderAdapter by lazy { SliderAdapter() }
    private var item = PopularItemModel()
    private var colorSelected: String = ""
    private var sizeSelected: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_detail,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        item = DetailFragmentArgs.fromBundle(requireArguments()).item

        binding.detailTitleTv.text = item.title
        binding.detailPriceTv.text = item.price.toString()
        binding.detailRatingTv.text = item.rating.toString()
        binding.descriptionTv.text = item.description

        banner()

        sizeListAdapter.submitList(item.size)
        colorListAdapter.submitList(item.picUrl)
        binding.colorListRv.adapter = colorListAdapter
        binding.sizeListRv.adapter = sizeListAdapter

        binding.backBtnDetail.setOnClickListener {
            findNavController().navigateUp()
        }

        colorListAdapter.onColorClick = {
            colorSelected = it
        }

        sizeListAdapter.onSizeSelected = {
            sizeSelected = it
        }

        binding.addToCartBtn.setOnClickListener {
            detailViewModel.addNewProduct(CartProduct(
                item,
                1,
                colorSelected,
                sizeSelected
            ))
        }

        lifecycleScope.launch {
            detailViewModel.addToCart.collectLatest {
                when(it){
                    is Resource.Loading-> {
                        binding.detailProgressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success-> {
                        binding.detailProgressBar.visibility = View.GONE
                        Toast.makeText(requireActivity(), "Added to your Cart", Toast.LENGTH_LONG).show()
                    }
                    is Resource.Failed-> {
                        binding.detailProgressBar.visibility = View.GONE
                        Log.d(DETAIL_TAG, "Error: ${it.message.toString()}")
                    }

                    else-> Unit
                }
            }
        }
    }

    private fun banner(){
        val sliderItems = ArrayList<SliderModel>()

        for (imageUrl in item.picUrl){
            sliderItems.add(SliderModel(imageUrl))
        }

        sliderAdapter.submitList(sliderItems)

        binding.viewPagerDetail.apply {
            adapter = sliderAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            clipToPadding = true
            clipChildren = true
            offscreenPageLimit = 1
        }
    }

}