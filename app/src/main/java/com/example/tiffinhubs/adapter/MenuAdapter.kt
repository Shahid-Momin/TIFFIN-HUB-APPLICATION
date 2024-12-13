package com.example.tiffinhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tiffinhubs.DetailsMenuActivity
import com.example.tiffinhubs.databinding.TiffinItemListBinding
import com.example.tiffinhubs.model.AllMenu


class MenuAdapter(
    private val menuItems: List<AllMenu>,
    private val context: Context
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(
            TiffinItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuItem = menuItems[position]
        holder.bind(menuItem)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailsMenuActivity::class.java).apply {
                putExtra("MenuItemName", menuItem.foodName)
                putExtra("MenuItemImage", menuItem.foodImage)
                putExtra("MenuItemDescription", menuItem.foodDescription) // Add this line
                putExtra("MenuItemIngredients", menuItem.foodIngredient) // Add this line
                putExtra("MenuItemPrice",menuItem.foodPrice)

            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = menuItems.size

    class MenuViewHolder(private val binding: TiffinItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menuItem: AllMenu) {
            binding.tiffinItemName.text = menuItem.foodName
            binding.priceMenu.text = menuItem.foodPrice
            Glide.with(binding.root.context)
                .load(menuItem.foodImage)
                .into(binding.tiffinItemImage)
        }
    }
}
