package com.android.developer.prof.reda.snapcart.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.developer.prof.reda.snapcart.R
import com.android.developer.prof.reda.snapcart.databinding.BrandItemsBinding
import com.android.developer.prof.reda.snapcart.model.BrandModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.request.RequestOptions

class BrandAdapter: ListAdapter<BrandModel, BrandAdapter.BrandViewHolder> (DiffCallback) {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1
    inner class BrandViewHolder(private val binding: BrandItemsBinding): ViewHolder(binding.root){
        fun bind(brandModel: BrandModel, position: Int){

            binding.brandNameTv.text = brandModel.title

            Glide.with(itemView)
                .load(brandModel.picUrl)
                .dontAnimate()
                .into(binding.brandImage)

            binding.root.setOnClickListener {
                lastSelectedPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(lastSelectedPosition)
                notifyItemChanged(selectedPosition)
            }

            binding.brandNameTv.setTextColor(itemView.context.resources.getColor(R.color.white))

            if (selectedPosition == position){
                binding.brandImage.setBackgroundResource(0)
                binding.brandMainLayout.setBackgroundResource(R.drawable.purple_bg)
                ImageViewCompat.setImageTintList(binding.brandImage,
                    ColorStateList.valueOf(itemView.context.getColor(R.color.white)))
                binding.brandNameTv.visibility = View.VISIBLE
            }else{
                binding.brandImage.setBackgroundResource(R.drawable.grey_bg)
                binding.brandMainLayout.setBackgroundResource(0)
                ImageViewCompat.setImageTintList(binding.brandImage,
                    ColorStateList.valueOf(itemView.context.getColor(R.color.black)))
                binding.brandNameTv.visibility = View.GONE
            }

        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<BrandModel>(){
        override fun areItemsTheSame(oldItem: BrandModel, newItem: BrandModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: BrandModel, newItem: BrandModel): Boolean {
            return oldItem.id == newItem.id
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        return BrandViewHolder(BrandItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }
    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        val brandModel = getItem(position)
        holder.bind(brandModel, position)


    }
}