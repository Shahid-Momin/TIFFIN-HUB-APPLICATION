package com.example.tiffinhubs

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tiffinhubs.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val navcontroller=findNavController(R.id.fragmentContainerView)
        val bottomNav=findViewById<BottomNavigationView>(R.id.bottomnavigation)
        bottomNav.setupWithNavController(navcontroller)
        binding.Notificationbell.setOnClickListener{
            val bottomSheetDialog=NotificationFragment()
            bottomSheetDialog.show(supportFragmentManager,"NotificationFragment")
        }

    }
}