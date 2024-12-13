package com.example.tiffinhubs.model


data class CartItems(
    val foodName: String? = null,
    val foodPrice: String? = null,
    val foodDescription: String? = null,
    val foodImage: String? = null, // Ensure this is a String
    val foodQuantity: Int? = null ,// Add quantity if needed
    val foodIngredient: String? = null,

)
