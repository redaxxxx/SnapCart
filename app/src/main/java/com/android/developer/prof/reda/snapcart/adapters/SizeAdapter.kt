package com.android.developer.prof.reda.snapcart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.developer.prof.reda.snapcart.R
import com.android.developer.prof.reda.snapcart.databinding.ColorViewHolderBinding
import com.android.developer.prof.reda.snapcart.databinding.SizeViewHolderBinding
import com.android.developer.prof.reda.snapcart.model.PopularItemModel
import com.bumptech.glide.Glide

class SizeAdapter : ListAdapter<String, SizeAdapter.SizeViewHolder>(DiffCallback) {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1
    inner class SizeViewHolder(private val binding: SizeViewHolderBinding): ViewHolder(binding.root){
        fun bind(size : String, position: Int){
            binding.sizeTv.text = size

            binding.root.setOnClickListener {
                lastSelectedPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(lastSelectedPosition)
                notifyItemChanged(selectedPosition)
                onSizeSelected?.invoke(size)
            }

            if (selectedPosition == position){
                binding.sizeLayout.setBackgroundResource(R.drawable.grey_bg_selected)
                binding.sizeTv.setTextColor(itemView.context.resources.getColor(R.color.purple))
            }else{
                binding.sizeLayout.setBackgroundResource(R.drawable.grey_bg)
                binding.sizeTv.setTextColor(itemView.context.resources.getColor(R.color.black))
            }
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem.length == newItem.length
        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        return SizeViewHolder(SizeViewHolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }

    var onSizeSelected : ((sizeSelected: String)-> Unit)? = null
}