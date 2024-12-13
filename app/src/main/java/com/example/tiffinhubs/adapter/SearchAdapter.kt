package com.example.tiffinhubs.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tiffinhubs.DetailsMenuActivity
import com.example.tiffinhubs.databinding.TiffinItemListBinding

class SearchAdapter (
    private val foodName: List<String>,
    private val image: List<Int>,
    private val price: List<String>,
    private val requireContext: Context
    ) : RecyclerView.Adapter<SearchAdapter.MenuViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
            return MenuViewHolder(
                TiffinItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

        override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
            val foodName = foodName[position]
            val image = image[position]
            val price = price[position]
            holder.bind(foodName, image, price)

            holder.itemView.setOnClickListener{
                val intent= Intent(requireContext, DetailsMenuActivity::class.java)
                intent.putExtra("MenuItemName",foodName)
                intent.putExtra("MenuItemImage",image)
                requireContext.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return foodName.size
        }

        class MenuViewHolder(private val binding: TiffinItemListBinding) : RecyclerView.ViewHolder(binding.root) {
            private val imagesview = binding.tiffinItemImage

            fun bind(foodName: String, image: Int, price: String) {
                binding.tiffinItemName.text = foodName
                binding.priceMenu.text = price
                imagesview.setImageResource(image)




            }
        }
    }
