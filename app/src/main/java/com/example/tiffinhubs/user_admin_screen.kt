package com.example.tiffinhubs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tiffinhubs.FirstAdminUse.MainActivity2Admin
import com.example.tiffinhubs.FirstAdminUse.firstadminlogin

class user_admin_screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_admin_screen)

        val userbutton=findViewById<Button>(R.id.userbutton)
        val Adminbutton=findViewById<Button>(R.id.Adminbutton)

        userbutton.setOnClickListener{
            val intent= Intent(this,user_login_page::class.java)
            startActivity(intent)
        }

        Adminbutton.setOnClickListener{
            val intent= Intent(this,firstadminlogin::class.java)
            startActivity(intent)
        }


    }
}