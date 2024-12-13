package com.example.tiffinhubs

import android.os.Bundle
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tiffinhubs.databinding.ActivityDetailsMenuBinding
import com.example.tiffinhubs.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsMenuBinding
    private lateinit var auth: FirebaseAuth

    // Class-level variables to hold food item details
    private var foodName: String? = null
    private var foodImage: String? = null
    private var foodDescription: String? = null
    private var foodIngredients: String? = null
    private var foodPrice: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Retrieve data from the intent and assign to class properties
        foodName = intent.getStringExtra("MenuItemName")
        foodImage = intent.getStringExtra("MenuItemImage")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodIngredients = intent.getStringExtra("MenuItemIngredients")
        foodPrice = intent.getStringExtra("MenuItemPrice")

        // Set the values to the views
        binding.detailedfoodname.text = foodName ?: "Unknown Food"

        // Use Glide to load the image
        Glide.with(this)
            .load(foodImage) // Load the image from URL or URI
            .placeholder(R.drawable.pizza) // Optional: set a placeholder image
            .error(R.drawable.pizza) // Optional: set an error image
            .into(binding.detailedfoodimage)

        binding.detailfooddescription.text = foodDescription ?: "No description available."
        binding.ingrediantfood.text = foodIngredients ?: "No ingredients listed."

        binding.comeBackarrowdetailed.setOnClickListener {
            finish()
        }
        binding.DeatailedItemAddButton.setOnClickListener {
            addItemToCart()
        }
    }

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid ?: ""

        Log.d("Price", "Food Price: $foodPrice")


        // Create a cartItem object using the assigned properties
        val cartItem = CartItems(
            foodName = foodName,
            foodPrice = foodPrice,
            foodDescription = foodDescription,
            foodImage = foodImage,
            foodQuantity = 1 // Default quantity
        )

        // Save data to cart item in Firebase
        database.child("user").child(userId).child("CartItems").push().setValue(cartItem)
            .addOnSuccessListener {
                Toast.makeText(this, "Item Added To Cart Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Item Not Added", Toast.LENGTH_SHORT).show()
            }
    }
}


    //2
//    private fun addItemToCart() {
//        val database = FirebaseDatabase.getInstance().reference
//        val userId = auth.currentUser?.uid ?: ""
//
//        if (userId.isEmpty()) {
//            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        // Create a reference to the user's cart items
//        val cartRef = database.child("user").child(userId).child("CartItems")
//
//        // Query to check if the item is already in the cart
//        cartRef.orderByChild("foodName").equalTo(foodName).limitToFirst(1).get()
//            .addOnSuccessListener { dataSnapshot ->
//                if (dataSnapshot.exists()) {
//                    // Item already exists in the cart
//                    for (itemSnapshot in dataSnapshot.children) {
//                        // Update the quantity
//                        val currentQuantity =
//                            itemSnapshot.child("foodQuantity").getValue(Int::class.java) ?: 0
//                        val newQuantity = currentQuantity + 1
//
//                        itemSnapshot.ref.child("foodQuantity").setValue(newQuantity)
//                            .addOnSuccessListener {
//                                Toast.makeText(
//                                    this,
//                                    "Item quantity updated in cart",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                            .addOnFailureListener { exception ->
//                                Log.e(
//                                    "FirebaseError",
//                                    "Error updating quantity: ${exception.message}"
//                                )
//                                Toast.makeText(
//                                    this,
//                                    "Failed to update item quantity",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                    }
//                } else {
//                    // Item does not exist, add it to the cart
//                    val cartItem = CartItems(
//                        foodName = foodName,
//                        foodPrice = foodPrice,
//                        foodDescription = foodDescription,
//                        foodImage = foodImage,
//                        foodQuantity = 1 // Default quantity
//                    )
//
//                    // Save data to cart item in Firebase
//                    cartRef.push().setValue(cartItem)
//                        .addOnSuccessListener {
//                            Toast.makeText(
//                                this,
//                                "Item added to cart successfully",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                        .addOnFailureListener { exception ->
//                            Log.e(
//                                "FirebaseError",
//                                "Error adding item to cart: ${exception.message}"
//                            )
//                            Toast.makeText(
//                                this,
//                                "Item not added: ${exception.message}",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.e("FirebaseError", "Error checking cart items: ${exception.message}")
//                Toast.makeText(this, "Error checking cart items", Toast.LENGTH_SHORT).show()
//            }
//    }
//}


    //3
//    private fun addItemToCart() {
//        val database = FirebaseDatabase.getInstance().reference
//        val userId = auth.currentUser?.uid ?: ""
//
//        if (userId.isEmpty()) {
//            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        // Reference to the user's cart items
//        val cartRef = database.child("user").child(userId).child("CartItems")
//
//        // Query to check if the item is already in the cart
//        cartRef.orderByChild("foodName").equalTo(foodName).limitToFirst(1).get()
//            .addOnSuccessListener { dataSnapshot ->
//                if (dataSnapshot.exists()) {
//                    // Item already exists in the cart
//                    for (itemSnapshot in dataSnapshot.children) {
//                        // Update the quantity
//                        val currentQuantity =
//                            itemSnapshot.child("foodQuantity").getValue(Int::class.java) ?: 0
//                        val newQuantity = currentQuantity + 1
//
//                        // Calculate the new total price
//                        val currentPrice =
//                            itemSnapshot.child("foodPrice").getValue(String::class.java)
//                                ?.toDoubleOrNull() ?: 0.0
//                        val newTotalPrice = currentPrice * newQuantity
//
//                        // Update quantity and total price
//                        itemSnapshot.ref.child("foodQuantity").setValue(newQuantity)
//                        itemSnapshot.ref.child("totalPrice").setValue(newTotalPrice)
//                            .addOnSuccessListener {
//                                Toast.makeText(
//                                    this,
//                                    "Item quantity updated in cart",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                            .addOnFailureListener { exception ->
//                                Log.e("FirebaseError", "Error updating item: ${exception.message}")
//                                Toast.makeText(this, "Failed to update item", Toast.LENGTH_SHORT)
//                                    .show()
//                            }
//                    }
//                } else {
//                    // Item does not exist, add it to the cart
//                    val foodPriceDouble = foodPrice?.toDoubleOrNull() ?: 0.0
//                    val cartItem = CartItems(
//                        foodName = foodName,
//                        foodPrice = foodPrice,
//                        foodDescription = foodDescription,
//                        foodImage = foodImage,
//                        foodQuantity = 1, // Default quantity
//
//                    )
//
//                    // Save data to cart item in Firebase
//                    cartRef.push().setValue(cartItem)
//                        .addOnSuccessListener {
//                            Toast.makeText(
//                                this,
//                                "Item added to cart successfully",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                        .addOnFailureListener { exception ->
//                            Log.e(
//                                "FirebaseError",
//                                "Error adding item to cart: ${exception.message}"
//                            )
//                            Toast.makeText(
//                                this,
//                                "Item not added: ${exception.message}",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.e("FirebaseError", "Error checking cart items: ${exception.message}")
//                Toast.makeText(this, "Error checking cart items", Toast.LENGTH_SHORT).show()
//            }
//    }
//}