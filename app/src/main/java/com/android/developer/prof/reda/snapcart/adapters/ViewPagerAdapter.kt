package com.android.developer.prof.reda.snapcart.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.android.developer.prof.reda.snapcart.R
import com.android.developer.prof.reda.snapcart.model.SliderModel
import com.bumptech.glide.Glide
import java.util.Objects

class ViewPagerAdapter(private val sliderImages: List<SliderModel>): PagerAdapter() {

    private lateinit var layoutInflater: LayoutInflater
    override fun getCount(): Int {
        return sliderImages.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = layoutInflater.inflate(
            R.layout.slider_item_container,
            container,
            false
        )

        val sliderImg = itemView.findViewById<ImageView>(R.id.imageSlide)

        Glide.with(itemView)
            .load(sliderImages[position])
            .into(sliderImg)

        Objects.requireNonNull(container).addView(itemView)

        return itemView
    }
}