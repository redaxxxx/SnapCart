package com.android.developer.prof.reda.snapcart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.developer.prof.reda.snapcart.databinding.CartItemBinding
import com.android.developer.prof.reda.snapcart.model.CartProduct
import com.bumptech.glide.Glide

class CartAdapter : ListAdapter<CartProduct, CartAdapter.CartViewHolder>(DiffCallback){

    inner class CartViewHolder(private val binding: CartItemBinding) : ViewHolder(binding.root){
        fun bind(cartProduct: CartProduct){
            Glide.with(itemView)
                .load(cartProduct.colorSelected)
                .into(binding.picCart)

            binding.titleCartTv.text = cartProduct.popularItemModel.title
            binding.feeEachItem.text = "$${cartProduct.popularItemModel.price}"
            binding.quantityNum.text = cartProduct.quantity.toString()

            var totalEachItem = "$${cartProduct.popularItemModel.price * cartProduct.quantity}"
            binding.plusBtn.setOnClickListener {
                totalEachItem = "$${cartProduct.popularItemModel.price * cartProduct.quantity}"
                onClickPlus?.invoke(cartProduct)
            }

            binding.minusBtn.setOnClickListener {
                totalEachItem = "$${cartProduct.popularItemModel.price * cartProduct.quantity}"
                onClickMinus?.invoke(cartProduct)
            }

            binding.totalEachItem.text = totalEachItem
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.popularItemModel == newItem.popularItemModel
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(CartItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    var onClickPlus: ((CartProduct) -> Unit)? = null
    var onClickMinus: ((CartProduct) -> Unit)? = null
}