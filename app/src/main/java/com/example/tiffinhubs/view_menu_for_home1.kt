package com.example.tiffinhubs

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiffinhub.adapter.View_Menu_item_adapter
import com.example.tiffinhubs.databinding.ActivityViewMenuForHome1Binding
import com.example.tiffinhubs.model.AllMenu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class view_menu_for_home1 : AppCompatActivity() {
    private lateinit var binding: ActivityViewMenuForHome1Binding
    private lateinit var databaseReference: DatabaseReference
    private var menuItems: ArrayList<AllMenu> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewMenuForHome1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().reference.child("menu")
        retrieveMenuItems()

        binding.goToHomepage.setOnClickListener {
            finish() // Navigate back to the previous activity
        }
    }

    private fun retrieveMenuItems() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItems.clear()
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(AllMenu::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DatabaseError", "Error: ${error.message}")
            }
        })
    }

    private fun setAdapter() {
        val adapter = View_Menu_item_adapter(menuItems, this)
        binding.rvViewMenu.layoutManager = LinearLayoutManager(this)
        binding.rvViewMenu.adapter = adapter
    }
}
