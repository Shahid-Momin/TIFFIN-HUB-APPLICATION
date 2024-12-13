package com.example.tiffinhubs.Adminadapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tiffinhubs.databinding.PendingOrdersItemBinding

class PendingOrderAdapter(private  val context: Context,
                          private val customerNames:MutableList<String>,
                          private val Quantity:MutableList<String>,
                          private val foodImage: MutableList<String>,
                          private val itemClicked: OnItemClicked
):
    RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {
        interface OnItemClicked{
            fun onItemClickListener(position: Int)
            fun onItemAcceptClickListener(position: Int)
            fun onItemDispatchClickListener(position: Int)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding=
            PendingOrdersItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int=customerNames.size

    inner class PendingOrderViewHolder(private val binding: PendingOrdersItemBinding): RecyclerView.ViewHolder(binding.root) {
        private var isAccepted=false
        fun bind(position: Int) {
            binding.apply {
                Admincustomername.text=customerNames[position]
                Adminfoodquantity.text=Quantity[position]
               // Adminpendingfoodimage.setImageResource(foodImage[position])
                val uriString=foodImage[position]
                val uri= Uri.parse(uriString)
                Glide.with(context).load(uri).into(Adminpendingfoodimage)


                pendingdeletebuttonaccepte.apply {
                    if (!isAccepted){
                        text="Accept"
                    }else{
                        text="Dispatch"
                    }
                    setOnClickListener{
                        if (!isAccepted){
                            text="Dispatch"
                            isAccepted=true
                            showToast("Order is Accepted")
                            itemClicked.onItemAcceptClickListener(position)
                        }else{
                            customerNames.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order is Dispatch")
                            itemClicked.onItemDispatchClickListener(position)

                        }
                    }
                }
                itemView.setOnClickListener{
                    itemClicked.onItemClickListener(position)
                }
            }
        }
        private fun showToast(message: String){
            Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
        }
    }
}