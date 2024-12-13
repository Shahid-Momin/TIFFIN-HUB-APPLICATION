package com.example.tiffinhubs.Fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tiffinhubs.R
import com.example.tiffinhubs.adapter.BuyAgainAdapter
import com.example.tiffinhubs.databinding.FragmentHistoryBinding
import com.example.tiffinhubs.model.OrderDetails
import com.example.tiffinhubs.recentOrderitems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOderItem: MutableList<OrderDetails> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        //retrive and display the user order history
        retrieveBuyHistory()
       // setupRecyclerview()

        binding.recentbuyitem.setOnClickListener{
            seeItemsRecentBuy()
        }
        binding.recivedButton.setOnClickListener{
            updateOrderStatus()
        }
        return binding.root
    }

    private fun updateOrderStatus() {
        val itemPushKey=listOfOderItem[0].itemPushKey
        val completeOrderReference=database.reference.child("CompletedOrder").child(itemPushKey!!)
        completeOrderReference.child("paymentReceived").setValue(true)
    }

    //function to see items recent buy
    private fun seeItemsRecentBuy() {
        listOfOderItem.firstOrNull()?.let { recentBuy->
            val intent=Intent(requireContext(),recentOrderitems::class.java)
            intent.putExtra("RecentBuyOrderItem",ArrayList(listOfOderItem))
            startActivity(intent)
        }
    }
//function to retrive items recent buy history
    private fun retrieveBuyHistory() {
        binding.recentbuyitem.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid ?: ""
        val buyItemReference: DatabaseReference =
            database.reference.child("user").child(userId).child("BuyHistory")
        val shortingQuery = buyItemReference.orderByChild("currentTime ")

        shortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children) {
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listOfOderItem.add(it)
                    }
                }
                listOfOderItem.reverse()
                if (listOfOderItem.isNotEmpty()) {
                    //display the most recent order detaail
                    setDataInRecentBuyItem()
                    //setup recyclerview with previous order details
                  setPreviousBuyItemsRecyclerView()
                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun setDataInRecentBuyItem() {
        binding.recentbuyitem.visibility = View.VISIBLE
        val recentOrderItem = listOfOderItem.firstOrNull()

        if (recentOrderItem != null) {
            with(binding) {
                buyagainfoodname.text = recentOrderItem.foodNames?.firstOrNull() ?: "No Name"
                buyagainfoodprice.text = recentOrderItem.foodPrices?.firstOrNull() ?: "0.00"
                val imageUrl = recentOrderItem.foodImages?.firstOrNull() ?: ""
                Glide.with(requireContext()).load(imageUrl).into(buyagainfoodimage)

                // Update order status and visibility based on acceptance
                if (recentOrderItem.orderAccepted == true) {
                    orderstatus.background.setTint(Color.GREEN)
                    recivedButton.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Get Product Successfully ", Toast.LENGTH_SHORT).show()
                } else {
                    orderstatus.background.setTint(Color.RED) // Optionally indicate unaccepted
                    recivedButton.visibility = View.GONE
                }
            }
        } else {
            binding.recentbuyitem.visibility = View.GONE // Hide if no recent order item is found
        }
    }


    private fun setPreviousBuyItemsRecyclerView() {
        val buyAgainFoodName = mutableListOf<String>()
        val buyAgainFoodPrice = mutableListOf<String>()
        val buyAgainFoodImages = mutableListOf<String>()
        for (i in 1 until listOfOderItem.size) {
            listOfOderItem[i].foodNames?.firstOrNull()?.let { buyAgainFoodName.add(it) }
            listOfOderItem[i].foodPrices?.firstOrNull()?.let { buyAgainFoodPrice.add(it) }
            listOfOderItem[i].foodImages?.firstOrNull()?.let {
                buyAgainFoodImages.add(it)
            }
        }
        val rv = binding.BuyAgainRecyclerview
        rv.layoutManager = LinearLayoutManager(requireContext())
        buyAgainAdapter = BuyAgainAdapter(
            buyAgainFoodName,
            buyAgainFoodPrice,
            buyAgainFoodImages,
            requireContext()
        )
        rv.adapter = buyAgainAdapter
    }



//    private fun setupRecyclerview(){
//
//        buyAgainAdapter=
//            BuyAgainAdapter(buyAgainFoodName,buyAgainFoodPrice,buyAgainFoodImages,requireContext())
//        binding.BuyAgainRecyclerview.adapter=buyAgainAdapter
//        binding.BuyAgainRecyclerview.layoutManager= LinearLayoutManager(requireContext())
//    }


}