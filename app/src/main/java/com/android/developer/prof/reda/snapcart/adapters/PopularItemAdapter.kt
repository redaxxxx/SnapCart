package com.android.developer.prof.reda.snapcart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.developer.prof.reda.snapcart.databinding.RecommendationItemsBinding
import com.android.developer.prof.reda.snapcart.model.PopularItemModel
import com.bumptech.glide.Glide

class PopularItemAdapter : ListAdapter<PopularItemModel, PopularItemAdapter.PopularItemViewHolder> (DiffCallback){

    inner class PopularItemViewHolder(private val binding: RecommendationItemsBinding) : ViewHolder(binding.root){
        fun bind(item: PopularItemModel){
            Glide.with(itemView)
                .load(item.picUrl[0])
                .into(binding.recomendImage)

            binding.titleTv.text = item.title
            binding.priceTv.text = item.price.toString()
            binding.ratingTv.text = item.rating.toString()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<PopularItemModel>(){
        override fun areItemsTheSame(
            oldItem: PopularItemModel,
            newItem: PopularItemModel
        ): Boolean {
            return newItem == oldItem
        }

        override fun areContentsTheSame(
            oldItem: PopularItemModel,
            newItem: PopularItemModel
        ): Boolean {
            return newItem.title == oldItem.title
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularItemViewHolder {
        return PopularItemViewHolder(RecommendationItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: PopularItemViewHolder, position: Int) {
        val popularItem = getItem(position)
        holder.bind(popularItem)
    }
}