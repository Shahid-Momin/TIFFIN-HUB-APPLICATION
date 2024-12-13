package com.example.tiffinhubs

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiffinhubs.adapter.RecentBuyAdapter
import com.example.tiffinhubs.databinding.ActivityRecentOrderitemsBinding
import com.example.tiffinhubs.model.OrderDetails

class recentOrderitems : AppCompatActivity() {

    private val binding:ActivityRecentOrderitemsBinding by lazy {
        ActivityRecentOrderitemsBinding.inflate(layoutInflater)
    }
    private lateinit var allFoodNames:ArrayList<String>
    private lateinit var allFoodImages:ArrayList<String>
    private lateinit var allFoodPrices:ArrayList<String>
    private lateinit var allFoodQuantities:ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.backButton.setOnClickListener{
            finish()
        }



        val recentOrderitems=intent.getSerializableExtra("RecentBuyOrderItem") as ArrayList<OrderDetails>
        recentOrderitems ?.let {orderDetails->
            if (orderDetails.isNotEmpty()){
                val recentOrderItem=orderDetails[0]

                allFoodNames=recentOrderItem.foodNames as ArrayList<String>
                allFoodImages=recentOrderItem.foodImages as ArrayList<String>
                allFoodPrices=recentOrderItem.foodPrices as ArrayList<String>
                allFoodQuantities=recentOrderItem.foodQuantities as ArrayList<Int>

            }

        }
        setAdapter()

    }

    private fun setAdapter() {
        val rv=binding.recyclerViewRecentBuy
        rv.layoutManager=LinearLayoutManager(this)
        val adapter=RecentBuyAdapter(this,allFoodNames,allFoodImages,allFoodPrices,allFoodQuantities)
        rv.adapter=adapter
    }
}