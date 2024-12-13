package com.example.tiffinhubs.FirstAdminUse

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiffinhubs.Adminadapter.PendingOrderAdapter
import com.example.tiffinhubs.databinding.ActivityAdminPendingOrderBinding
import com.example.tiffinhubs.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Admin_PendingOrderActivity : AppCompatActivity(),PendingOrderAdapter.OnItemClicked {

    private var listOfName: MutableList<String> = mutableListOf()
    private var listOfTotalPrice: MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrder: MutableList<String> = mutableListOf()
    private var listOfOrderItem: ArrayList<OrderDetails> = arrayListOf() // Use OrderDetails
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference

    private val binding: ActivityAdminPendingOrderBinding by lazy {
        ActivityAdminPendingOrderBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Initialization of Firebase
        database = FirebaseDatabase.getInstance()
        databaseOrderDetails = database.reference.child("OrderDetails")
        getOrdersDetails()

        binding.addandbackpendingorder.setOnClickListener {
            finish()
        }
    }

    private fun getOrdersDetails() {
        databaseOrderDetails.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                    // Use orderDetails here instead of OrderDetails
                    orderDetails?.let {
                        listOfOrderItem.add(it)
                    }
                }
                addDataToListForRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })
    }

    private fun addDataToListForRecyclerView() {
        for (orderItem in listOfOrderItem) {
            orderItem.userName?.let { listOfName.add(it) }
            orderItem.totalPrice?.let { listOfTotalPrice.add(it) }
            orderItem.foodImages?.filterNot { it.isEmpty() }?.forEach {
                listOfImageFirstFoodOrder.add(it)
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        binding.AdminpendingorderRecyclerview.layoutManager = LinearLayoutManager(this)
        val adapter = PendingOrderAdapter(this, listOfName, listOfTotalPrice, listOfImageFirstFoodOrder,this)
        binding.AdminpendingorderRecyclerview.adapter = adapter
    }

    override fun onItemClickListener(position: Int) {
        val intent=Intent(this,OrderDetailsActivity::class.java)
        val userOrderDetails=listOfOrderItem[position]
        intent.putExtra("UserOrderDetails",userOrderDetails)
        startActivity(intent)
    }

    override fun onItemAcceptClickListener(position: Int) {
        //handle item acceptance and update  database
        val childItemPushKey=listOfOrderItem[position].itemPushKey
        val clickItemOrderRefrencae=childItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        clickItemOrderRefrencae?.child("orderAccepted")?.setValue(true)
        updateOrderAcceptStatus(position)

    }

    override fun onItemDispatchClickListener(position: Int) {
        //handle item dispatch and update  database
        val dispatchItemPushKey=listOfOrderItem[position].itemPushKey
        val dispatchItemOrderFreferance=database.reference.child("CompletedOrder").child(dispatchItemPushKey!!)
        dispatchItemOrderFreferance.setValue(listOfOrderItem[position])
            .addOnSuccessListener {
                deleteThisItemFromOrderDetails(dispatchItemPushKey)
            }
    }

    private fun deleteThisItemFromOrderDetails(dispatchItemPushKey: String){
        val orderDetailsItemsReference=database.reference.child("OrderDetails").child(dispatchItemPushKey)
        orderDetailsItemsReference.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Order is Dispatched", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Order is not Dispatched", Toast.LENGTH_SHORT).show()
            }

    }

    private fun updateOrderAcceptStatus (position: Int){
//update order acceptance in user Buy history and order details
        val userIdOfClickedItem=listOfOrderItem[position].userId
        val pushKeyOfClickedItem=listOfOrderItem[position].itemPushKey
        val buyHistoryReference=database.reference.child("user").child(userIdOfClickedItem!!).child("BuyHistory").child(pushKeyOfClickedItem!!)
        buyHistoryReference.child("orderAccepted").setValue(true)
        databaseOrderDetails.child(pushKeyOfClickedItem).child("orderAccepted").setValue(true)
    }
} 