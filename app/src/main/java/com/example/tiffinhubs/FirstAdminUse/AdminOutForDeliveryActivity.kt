package com.example.tiffinhubs.FirstAdminUse

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiffinhubs.Adminadapter.DeliveryAdapter
import com.example.tiffinhubs.R
import com.example.tiffinhubs.databinding.ActivityAdminOutForDeliveryBinding
import com.example.tiffinhubs.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminOutForDeliveryActivity : AppCompatActivity() {
    private val binding:ActivityAdminOutForDeliveryBinding by lazy { ActivityAdminOutForDeliveryBinding.inflate(layoutInflater) }
    private lateinit var database: FirebaseDatabase
    private  var listOfCompleteOrderList:ArrayList<OrderDetails> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.addandbackout.setOnClickListener{
            finish()
        }
    //retrive and display completed order
        retrieveCompleteOrderDetail()

//        val customerName= arrayListOf(
//            "john Doe",
//            "jane Smith",
//            "Mike johnson"
//        )
//
//        val moneyStatus= arrayListOf(
//            "recived",
//            "notRecived",
//            "Pending"
//        )

//        val adapter= DeliveryAdapter()
//        binding.DeliveryRecyclerview.adapter=adapter
//        binding.DeliveryRecyclerview.layoutManager= LinearLayoutManager(this)

    }

    private fun retrieveCompleteOrderDetail() {
        //initalize firebas database
        database=FirebaseDatabase.getInstance()
        val completeOrderReference=database.reference.child("CompletedOrder")
            .orderByChild("currentTime")
        completeOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear the list before populating it with new data
                listOfCompleteOrderList.clear()
                for (orderSnapshot in snapshot.children){
                    val completeOrder=orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let {
                        listOfCompleteOrderList.add(it)
                    }

                }
                //reverse the list to display latest order first
                listOfCompleteOrderList.reverse()

                setDataIntoRecyclerView()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", error.message)
            }
        })
    }
    private fun setDataIntoRecyclerView() {
        //initialization to hold customer name and payment status
        val customerName= mutableListOf<String>()
        val moneyStatus= mutableListOf<Boolean>()

        for (order in listOfCompleteOrderList){
            order.userName?.let {
                customerName.add(it)
            }
            moneyStatus.add(order.paymentReceived?:false)
        }
        val adapter= DeliveryAdapter(customerName,moneyStatus)
        binding.DeliveryRecyclerview.adapter=adapter
        binding.DeliveryRecyclerview.layoutManager= LinearLayoutManager(this)

    }
}