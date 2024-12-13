package com.example.tiffinhubs.FirstAdminUse

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tiffinhubs.R
import com.example.tiffinhubs.databinding.ActivityMainActivity2AdminBinding
import com.example.tiffinhubs.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity2Admin : AppCompatActivity() {
    private val binding: ActivityMainActivity2AdminBinding by lazy {
        ActivityMainActivity2AdminBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completedOrderReference:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.addmenuitem.setOnClickListener {
            val intent = Intent(this, Admin_AdditemActivity::class.java)
            startActivity(intent)
        }

        binding.ViewAllMenu.setOnClickListener {
            val intent = Intent(this, Admin_AllitemActivity::class.java)
            startActivity(intent)
        }

        binding.outForDeliveryButton.setOnClickListener {
            val intent = Intent(this, AdminOutForDeliveryActivity::class.java)
            startActivity(intent)
        }

        binding.AdminProfile.setOnClickListener {
            val intent = Intent(this, Admin_ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.AdminCreatenewusers.setOnClickListener {
            val intent = Intent(this, Admin_CreateNewVendorActivity::class.java)
            startActivity(intent)
        }

        binding.Pendingorderbutton.setOnClickListener {
            val intent = Intent(this, Admin_PendingOrderActivity::class.java)
            startActivity(intent)
        }

        binding.imageView9.setOnClickListener {
            val intent = Intent(this, Admin_PendingOrderActivity::class.java)
            startActivity(intent)
        }
        binding.logOutButton.setOnClickListener{
            auth.signOut()
            startActivity(Intent(this,firstadminsignup::class.java))
        }

        pendingOrders()
        completedOrders()
        wholeTimeEarning()
    }

    private fun wholeTimeEarning() {
        var listOfTotalPay= mutableListOf<Int>()
        completedOrderReference=FirebaseDatabase.getInstance().reference.child("CompletedOrder")
        completedOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               for (orderSnapshot in snapshot.children){
                   var completeOrder =orderSnapshot.getValue(OrderDetails::class.java)

                   completeOrder?.totalPrice?.replace("$","")?.toIntOrNull()
                       ?.let { i ->
                           listOfTotalPay.add(i)
                       }
               }
                binding.WholeTimeEarning.text= listOfTotalPay.sum().toString() +"$"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun completedOrders() {
        val completedOrderReference = database.reference.child("CompletedOrder")
        var completedOrderItemCount = 0
        completedOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                completedOrderItemCount=snapshot.childrenCount.toInt()
                binding.completeOrders.text=completedOrderItemCount.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun pendingOrders() {
        database=FirebaseDatabase.getInstance()
        val pendingOrderReference = database.reference.child("OrderDetails")
        var pendingOrderItemCount = 0
        pendingOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrderItemCount=snapshot.childrenCount.toInt()
                binding.pendingOrders.text=pendingOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
}