package com.example.tiffinhubs.Adminadapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tiffinhubs.databinding.AdminResourceMenuitemBinding
import com.example.tiffinhubs.model.AllMenu
import com.google.firebase.database.DatabaseReference

class MenuitemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference,
    private val onDeleteClickListener:(position :Int)-> Unit
):
    RecyclerView.Adapter<MenuitemAdapter.AddItemViewHolder>() {
    private val itemQuantities= IntArray(menuList.size){1}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding=
            AdminResourceMenuitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AddItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int=menuList.size

    inner class AddItemViewHolder(private val binding: AdminResourceMenuitemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity=itemQuantities[position]
                val menuItem=menuList[position]
                val uriString=menuItem.foodImage
                val uri= Uri.parse(uriString)
                AdminAllItemVendorname.text=menuItem.vendorMenu
                Adminfoodname.text=menuItem.foodName
                Adminfoodprice.text=menuItem.foodPrice
                Glide.with(context).load(uri).into(Adminfoodimageview)

                //Adminfoodimageview.setImageResource(menuList[position])

                Admincountity.text=quantity.toString()
                Adminminusbutton05.setOnClickListener{
                    decreaseQuantity(position)
                }
                Adminplusbutton06.setOnClickListener {
                    increaseQuantity(position)
                }
                Admintrashbin.setOnClickListener {
                    onDeleteClickListener(position)
                }


            }
        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantities[position]<10){
                itemQuantities[position]++
                binding.Admincountity.text=itemQuantities[position].toString()
            }
        }
        private fun decreaseQuantity(position: Int) {
            if (itemQuantities[position]>1){
                itemQuantities[position]--
                binding.Admincountity.text=itemQuantities[position].toString()
            }
        }
        private fun deleteQuantity(position: Int) {
           // menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,menuList.size)
        }

    }
}