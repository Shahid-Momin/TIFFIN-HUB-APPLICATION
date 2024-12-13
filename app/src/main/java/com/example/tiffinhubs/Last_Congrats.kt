package com.example.tiffinhubs

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tiffinhubs.databinding.ActivityLastCongratsBinding

class Last_Congrats : AppCompatActivity() {
    lateinit var bindind: ActivityLastCongratsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindind= ActivityLastCongratsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(bindind.root)
        bindind.gohomecongrats.setOnClickListener{
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}