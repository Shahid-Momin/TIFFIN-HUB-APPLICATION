package com.example.tiffinhubs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class StartPage_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start_page)

        val nextbutton=findViewById<Button>(R.id.nextbutton)
        nextbutton.setOnClickListener{
            val intent= Intent(this,user_admin_screen::class.java)
            startActivity(intent)
        }

    }
}