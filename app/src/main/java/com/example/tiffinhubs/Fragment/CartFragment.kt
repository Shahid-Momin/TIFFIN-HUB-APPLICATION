package com.example.tiffinhubs.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiffinhubs.PayOuutActivity
import com.example.tiffinhubs.adapter.CartAdapter
import com.example.tiffinhubs.databinding.FragmentCartBinding
import com.example.tiffinhubs.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartFragment : Fragment() {
    private  lateinit var binding: FragmentCartBinding
    private  lateinit var auth: FirebaseAuth
    private  lateinit var database: FirebaseDatabase
    private  lateinit var foodNames: MutableList<String>
    private lateinit var foodPrices:MutableList<String>
    private lateinit var foodDescriptions:MutableList<String>
    private lateinit var foodImageUri: MutableList<String>
    private lateinit var foodIngredients: MutableList<String>
    private lateinit var quantity:MutableList<Int>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var userId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        auth=FirebaseAuth.getInstance()
        reteriveCartItems()


//as per video
//        val cartFoodName= listOf("burger","sandwich","momo","item")
//        val cartItemPrice= listOf("Rs.52","Rs.82","Rs.72","Rs.92")
//        val cartImage= listOf(R.drawable.plainrice,R.drawable.aloo_gobi,R.drawable.palak_panner,R.drawable._jh6tcac4wnlmnjdgce7hvs1qv1__1_)
//        val adapter= CartAdapter(ArrayList(cartFoodName),ArrayList(cartItemPrice),ArrayList(cartImage))
//        binding.cartRecyclerview.layoutManager= LinearLayoutManager(requireContext())
//        binding.cartRecyclerview.adapter=adapter

        binding.ProccedToOrder.setOnClickListener{
            //get order item details before proceeding  to check out
            getOrderItemsDetail()

        }
        return binding.root
    }

    private fun getOrderItemsDetail() {
        val orderIdReference:DatabaseReference=database.reference.child("user").child(userId).child("CartItems")
        val foodName= mutableListOf<String>()
        val foodPrice= mutableListOf<String>()
        val foodImage= mutableListOf<String>()
        val foodDescription= mutableListOf<String>()
        val foodIngredient= mutableListOf<String>()
        //get items quantities
        val foodQuantities=cartAdapter.getUpdatedItemsQuantities()

        orderIdReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for (foodSnapshot in snapshot.children){
                    //get the cartitem to respectiv list
                    val orderItems=foodSnapshot.getValue(CartItems::class.java)
                    //add items details in to list
                    orderItems?.foodName?.let { foodName.add(it) }
                    orderItems?.foodPrice?.let { foodPrice.add(it) }
                    orderItems?.foodDescription?.let { foodDescription.add(it) }
                    orderItems?.foodImage?.let { foodImage.add(it) }
                    orderItems?.foodIngredient?.let { foodIngredient.add(it) }
                }
                orderNow(foodName,foodPrice,foodDescription,foodImage,foodIngredient,foodQuantities)
            }



            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "order making failed,please try Again", Toast.LENGTH_SHORT).show()
            }
        })

    }
    private fun orderNow(foodName: MutableList<String>,
                         foodPrice: MutableList<String>,
                         foodDescription: MutableList<String>,
                         foodImage: MutableList<String>,
                         foodIngredient: MutableList<String>,
                         foodQuantities: MutableList<Int>) {
        if (isAdded && context !=null){
            val intent=Intent(requireContext(),PayOuutActivity::class.java)
            intent.putExtra("FoodItemName",foodName as ArrayList<String>)
            intent.putExtra("FoodItemPrice",foodPrice as ArrayList<String>)
            intent.putExtra("FoodItemImage",foodImage as ArrayList<String>)
            intent.putExtra("FoodItemDescription",foodDescription as ArrayList<String>)
            intent.putExtra("FoodItemIngredient",foodIngredient as ArrayList<String>)
            intent.putExtra("FoodItemQuantities",foodQuantities as ArrayList<Int>)
            startActivity(intent)
        }

    }

    private fun reteriveCartItems() {
        //database reference to the firebase
        database=FirebaseDatabase.getInstance()
        userId=auth.currentUser?.uid?:""
        //val foodReference:DatabaseReference=database.reference.child("user").child("CartItems")
        val foodReference: DatabaseReference = database.reference.child("user").child(userId).child("CartItems")

        //list to store cart items
        foodNames= mutableListOf()
        foodPrices= mutableListOf()
        foodDescriptions= mutableListOf()
        foodImageUri= mutableListOf()
        foodIngredients= mutableListOf()
        quantity= mutableListOf()

        //fetch the data from database
        foodReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children){

                    //get the cartitem object from the child node
                    val cartItems=foodSnapshot.getValue(CartItems::class.java)

                    //add cart  item detailed  to the list
                    cartItems?.foodName?.let { foodNames.add(it) }
                    cartItems?.foodPrice?.let { foodPrices.add(it) }
                    cartItems?.foodDescription?.let { foodDescriptions.add(it) }
                    cartItems?.foodImage?.let { foodImageUri.add(it) }
                    cartItems?.foodQuantity?.let { quantity.add(it) }
                    cartItems?.foodIngredient?.let { foodIngredients.add(it) }



                }
                setAdapter()
            }

            private fun setAdapter() {

                cartAdapter=CartAdapter(requireContext(),foodNames,foodPrices,foodDescriptions,foodImageUri,quantity,foodIngredients)
                binding.cartRecyclerview.layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                binding.cartRecyclerview.adapter=cartAdapter


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "data not fetch", Toast.LENGTH_SHORT).show()
            }

        })
    }

    companion object {

    }
}