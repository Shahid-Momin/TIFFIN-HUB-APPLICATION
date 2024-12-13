package com.example.tiffinhubs.FirstAdminUse

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiffinhubs.Adminadapter.MenuitemAdapter
import com.example.tiffinhubs.databinding.ActivityAdminAllitemBinding
import com.example.tiffinhubs.model.AllMenu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Admin_AllitemActivity : AppCompatActivity() {
    private lateinit var databaseReference:DatabaseReference
    private lateinit var database: FirebaseDatabase
    private  var menuItems:ArrayList<AllMenu> = ArrayList()
    private val binding: ActivityAdminAllitemBinding by lazy {
        ActivityAdminAllitemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        databaseReference=FirebaseDatabase.getInstance().reference
        retrieveMenuItem()

//        val menuFoodName = arrayListOf("sandwich", "rice", "aloo-gobi", "pizza", "palak-paneer")
//        val menuItemPrice = arrayListOf("Rs.87", "Rs.67", "Rs.47", "Rs.98", "Rs.120")
//        val menuFoodImage = arrayListOf(
//            R.drawable.sandwich,
//            R.drawable.plainrice,
//            R.drawable.aloo_gobi,
//            R.drawable.pizza,
//            R.drawable.palak_panner
//        )

        binding.addandback.setOnClickListener{
            finish()
        }

//        val adapter = MenuitemAdapter( ArrayList(menuFoodName), ArrayList(menuItemPrice), ArrayList(menuFoodImage)
//        )
//        binding.AdminMenurecyclerview.layoutManager = LinearLayoutManager(this)
//        binding.AdminMenurecyclerview.adapter = adapter


    }

    private fun retrieveMenuItem() {
        database=FirebaseDatabase.getInstance()
        val foodRef:DatabaseReference=database.reference.child("menu")

        //fetch data from database
        foodRef.addListenerForSingleValueEvent(object :ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                //clear existing data before populating
                menuItems.clear()

                //loop for through each food item
                for(foodSnapshot in snapshot.children ){
                    val menuItem=foodSnapshot.getValue(AllMenu::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError", "Error:${error.message} ")
            }
        })
    }
    private fun setAdapter() {
        val adapter = MenuitemAdapter(this@Admin_AllitemActivity,menuItems,databaseReference){position->
            deleteMenuItems(position)
        }
        binding.AdminMenurecyclerview.layoutManager = LinearLayoutManager(this)
        binding.AdminMenurecyclerview.adapter = adapter
    }

    private fun deleteMenuItems(position: Int) {
        val menuItemToDelete=menuItems[position]
        val menuItemKey=menuItemToDelete.key
        val foodMenuReference=database.reference.child("menu").child(menuItemKey!!)
        foodMenuReference.removeValue().addOnCompleteListener { task->
            if (task.isSuccessful){
                menuItems.removeAt(position)
                binding.AdminMenurecyclerview.adapter?.notifyItemRemoved(position)
            }else{
                Toast.makeText(this, "Item not deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}