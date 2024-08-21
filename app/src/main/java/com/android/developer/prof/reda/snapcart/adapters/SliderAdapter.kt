package com.android.developer.prof.reda.snapcart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.developer.prof.reda.snapcart.R
import com.android.developer.prof.reda.snapcart.model.SliderModel
import com.android.developer.prof.reda.snapcart.databinding.SliderItemContainerBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.request.RequestOptions

class SliderAdapter: ListAdapter<SliderModel, SliderAdapter.SliderViewHolder> (DiffCallback){

    class SliderViewHolder(private val binding: SliderItemContainerBinding): ViewHolder(binding.root){

        fun bind(sliderItem: SliderModel){
            val requestOptions = RequestOptions().transform(CenterInside())

            Glide.with(itemView)
                .load(sliderItem.url)
                .apply(requestOptions)
                .placeholder(R.drawable.banner1)
                .dontAnimate()
                .into(binding.imageSlide)
        }

    }

    companion object DiffCallback: DiffUtil.ItemCallback<SliderModel>(){
        override fun areItemsTheSame(oldItem: SliderModel, newItem: SliderModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SliderModel, newItem: SliderModel): Boolean {
            return oldItem.url == newItem.url
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(SliderItemContainerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val slider = getItem(position)
        holder.bind(slider)
    }
}