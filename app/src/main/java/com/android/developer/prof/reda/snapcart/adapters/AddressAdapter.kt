package com.android.developer.prof.reda.snapcart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.developer.prof.reda.snapcart.databinding.AddressViewHolderBinding
import com.android.developer.prof.reda.snapcart.model.Address

class AddressAdapter : ListAdapter<Address, AddressAdapter.AddressViewHolder>(DiffCallback){

    inner class AddressViewHolder(private val binding: AddressViewHolderBinding): ViewHolder(binding.root){
        fun bind(address: Address){
            //bind views
            binding.apply {
                fullName.text = address.fullName
                address1Tv.text = address.address
                cityTv.text = address.city
                regionTv.text = address.state
                postalCodeTv.text = address.zipCode
                countryTv.text = address.country

                //what happen when chick box is checked

            }
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Address>(){
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.fullName == newItem.fullName
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(AddressViewHolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = getItem(position)
        holder.bind(address)
    }
}