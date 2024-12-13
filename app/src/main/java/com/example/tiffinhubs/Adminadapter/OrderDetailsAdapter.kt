package com.example.tiffinhubs.Adminadapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tiffinhubs.databinding.OrderDetailItemBinding

class OrderDetailsAdapter(private var context: Context,
    private var foodNames:MutableList<String>,
    private var foodImages: MutableList<String>,
    private var foodQuantitys:MutableList<Int>,
    private var foodPrices:MutableList<String>
    ):RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
       val binding=OrderDetailItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int =foodNames.size
    inner class OrderDetailsViewHolder(private val binding: OrderDetailItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                foodName.text=foodNames[position]
                foodQuantity.text=foodQuantitys[position].toString()
                val uriString=foodImages[position]
                val uri= Uri.parse(uriString)
                Glide.with(context).load(uri).into(FoodImage)
                foodPrice.text=foodPrices[position]
            }

        }

    }
}