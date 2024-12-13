package com.example.tiffinhubs

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiffinhub.adapter.MenuAdapter
import com.example.tiffinhubs.databinding.ActivityVendorMenu01Binding
import com.example.tiffinhubs.model.AllMenu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Vendor_menu01 : AppCompatActivity() {
    private lateinit var binding: ActivityVendorMenu01Binding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var adapter: MenuAdapter
    private var menuItems: ArrayList<AllMenu> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendorMenu01Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val vendorName = intent.getStringExtra("VENDOR_NAME") ?: "Unknown Vendor"
        binding.vendorName.text = vendorName

        // Initialize Firebase Database reference for the specific vendor
        databaseReference = FirebaseDatabase.getInstance().reference.child("menu")

        setupRecyclerView()
        retrieveMenuItems()

        binding.comeBackarrow.setOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        adapter = MenuAdapter(menuItems, this)
        binding.rvVendorMenu.layoutManager = LinearLayoutManager(this)
        binding.rvVendorMenu.adapter = adapter
    }

    private fun retrieveMenuItems() {
        // Adjust this to get only the items that belong to the selected vendor
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItems.clear()
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(AllMenu::class.java)
                    // Check if the menuItem is not null and matches the vendorName
                    if (menuItem != null && menuItem.vendorMenu == binding.vendorName.text.toString()) {
                        menuItems.add(menuItem)
                        Log.d("MenuItem", "Added: ${menuItem.foodName}")
                    } else {
                        Log.e("MenuItem", "Menu item is null or vendor does not match")
                    }
                }
                if (menuItems.isEmpty()) {
                    Log.e("MenuItem", "No items found for vendor: ${binding.vendorName.text}")
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DatabaseError", "Error: ${error.message}")
            }
        })
    }


}

