package com.example.tiffinhubs.FirstAdminUse

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiffinhubs.Adminadapter.OrderDetailsAdapter
import com.example.tiffinhubs.R
import com.example.tiffinhubs.databinding.ActivityOrderDetailsBinding
import com.example.tiffinhubs.model.OrderDetails

class OrderDetailsActivity : AppCompatActivity() {
    private val binding:ActivityOrderDetailsBinding by lazy{
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }

    private var userName:String?=null
    private var address : String?=null
    private var phoneNumber:String?=null
    private var totalPrice:String?=null
    private  var foodNames:MutableList<String> = mutableListOf()
    private  var foodImages:MutableList<String> = mutableListOf()
    private  var foodQuantity:MutableList<Int> = mutableListOf()
    private  var foodPrices:MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.backButton.setOnClickListener{
            finish()
        }
        getDataFromIntent()

    }

    private fun getDataFromIntent() {
        val recivedOrderDetails=intent.getSerializableExtra("UserOrderDetails") as OrderDetails
        if (recivedOrderDetails !=null){
            userName=recivedOrderDetails.userName
            foodNames=recivedOrderDetails.foodNames!!
            foodImages=recivedOrderDetails.foodImages!!
            foodQuantity=recivedOrderDetails.foodQuantities!!
            address=recivedOrderDetails.address
            phoneNumber=recivedOrderDetails.phoneNumber
            foodPrices=recivedOrderDetails.foodPrices!!
            totalPrice=recivedOrderDetails.totalPrice

            setUserDetail()
            setAdapter()
        }

    }


    private fun setUserDetail() {
        binding.name.text=userName
        binding.address.text=address
        binding.phone.text=phoneNumber
        binding.totalPay.text=totalPrice
    }

    private fun setAdapter() {
        binding.orderDetailRecyclerView.layoutManager=LinearLayoutManager(this)
        val adapter=OrderDetailsAdapter(this,foodNames,foodImages,foodQuantity,foodPrices)
        binding.orderDetailRecyclerView.adapter=adapter

    }
}