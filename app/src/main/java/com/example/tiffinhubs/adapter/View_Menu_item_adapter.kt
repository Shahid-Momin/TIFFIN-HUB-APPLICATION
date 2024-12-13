package com.example.tiffinhub.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tiffinhubs.DetailsMenuActivity
import com.example.tiffinhubs.R
import com.example.tiffinhubs.databinding.ViewMenuItemlistBinding
import com.example.tiffinhubs.model.AllMenu

class View_Menu_item_adapter(
    private val menuItems: List<AllMenu>,
    private val context: Context
) : RecyclerView.Adapter<View_Menu_item_adapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ViewMenuItemlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menuItems[position])
    }

    override fun getItemCount(): Int = menuItems.size

    inner class MenuViewHolder(private val binding: ViewMenuItemlistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menuItem: AllMenu) {
            binding.viewMenuItemVendorname.text = menuItem.vendorMenu ?: "Unknown Vendor"
            binding.viewMenuItemName.text = menuItem.foodName ?: "Unknown Food"
            binding.viewMenuItemPrice.text = menuItem.foodPrice ?: "N/A"

            // Load image using Glide
            menuItem.foodImage?.let { imageUri ->
                Glide.with(context).load(imageUri).into(binding.viewMenuItemImage)
            } ?: binding.viewMenuItemImage.setImageResource(R.drawable.plainrice) // Use a placeholder image if necessary

            binding.root.setOnClickListener {
                val intent = Intent(context, DetailsMenuActivity::class.java).apply {
                    putExtra("MenuItemName", menuItem.foodName)
                    putExtra("MenuItemImage", menuItem.foodImage)
                    putExtra("MenuItemDescription", menuItem.foodDescription)
                    putExtra("MenuItemIngredients", menuItem.foodIngredient)
                    putExtra("MenuItemPrice",menuItem.foodPrice)
                }
                context.startActivity(intent)
            }
        }
    }
}
