package com.android.developer.prof.reda.snapcart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.developer.prof.reda.snapcart.R
import com.android.developer.prof.reda.snapcart.databinding.ColorViewHolderBinding
import com.android.developer.prof.reda.snapcart.model.PopularItemModel
import com.bumptech.glide.Glide

class ColorAdapter : ListAdapter<String, ColorAdapter.ColorViewHolder>(DiffCallback) {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1
    inner class ColorViewHolder(private val binding: ColorViewHolderBinding): ViewHolder(binding.root){
        fun bind(picUrl: String, position: Int){
            Glide.with(itemView)
                .load(picUrl)
                .into(binding.pic)

            binding.root.setOnClickListener {
                lastSelectedPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(lastSelectedPosition)
                notifyItemChanged(selectedPosition)
                onColorClick?.invoke(picUrl)
            }

            if (selectedPosition == position){
                binding.colorLayout.setBackgroundResource(R.drawable.grey_bg_selected)

            }else{
                binding.colorLayout.setBackgroundResource(R.drawable.grey_bg)
            }

        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder(ColorViewHolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val picUrl = getItem(position)
        holder.bind(picUrl, position)
    }

    var onColorClick: ((imageSelected: String)-> Unit)? = null
}