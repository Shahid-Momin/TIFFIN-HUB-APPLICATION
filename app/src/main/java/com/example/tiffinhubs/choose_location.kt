package com.example.tiffinhubs

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tiffinhubs.databinding.ActivityChooseLocationBinding

class choose_location : AppCompatActivity() {
    private val binding:ActivityChooseLocationBinding by lazy {
        ActivityChooseLocationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val locationlist= arrayOf("Mumbai","Bhiwandi","Thana","Kalyan")
        val adapter= ArrayAdapter(this, R.layout.simple_list_item_1,locationlist)
        val autoCompleteTextView=binding.LocationOfList
        autoCompleteTextView.setAdapter(adapter)

        binding.locationSelect.setOnClickListener{
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }
}