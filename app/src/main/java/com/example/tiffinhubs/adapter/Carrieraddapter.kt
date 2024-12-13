package com.example.tiffinhubs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tiffinhubs.R
import com.example.tiffinhubs.databinding.TiffinCarrierBinding
import com.example.tiffinhubs.model.VendorModel

class Carrieraddapter(
    private val vendors: List<VendorModel>,
    private val onItemClick: (Int) -> Unit // Click listener
) : RecyclerView.Adapter<Carrieraddapter.CarrierViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarrierViewHolder {
        return CarrierViewHolder(TiffinCarrierBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemClick)
    }

    override fun onBindViewHolder(holder: CarrierViewHolder, position: Int) {
        holder.bind(vendors[position], position)
    }

    override fun getItemCount(): Int {
        return vendors.size
    }

    class CarrierViewHolder(
        private val binding: TiffinCarrierBinding,
        private val onItemClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(vendor: VendorModel, position: Int) {
            binding.vendorNameCreate.text = vendor.vendorName
            binding.vendorMenuList.text = vendor.vendorTypesOfFood
            binding.vendorAddres.text = vendor.vendorAddress
            // Assuming you have a method to load images, e.g., using Glide or Picasso
            // Glide.with(binding.vendorImage.context).load(vendor.vendorImage).into(binding.vendorImage)

            // Load the vendor image using Glide
            Glide.with(binding.vendorImage.context)
                .load(vendor.vendorImage) // Assuming vendorImage contains the URL
                .placeholder(R.drawable.pizza) // Optional placeholder image
                .error(R.drawable.pizza) // Optional error image
                .into(binding.vendorImage)

            itemView.setOnClickListener {
                onItemClick(position)
            }
        }
    }
}
