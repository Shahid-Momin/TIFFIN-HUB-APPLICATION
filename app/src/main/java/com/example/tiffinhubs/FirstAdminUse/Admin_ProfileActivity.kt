package com.example.tiffinhubs.FirstAdminUse

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tiffinhubs.R
import com.example.tiffinhubs.databinding.ActivityAdminProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Admin_ProfileActivity : AppCompatActivity() {
    private val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
//    private lateinit var auth: FirebaseAuth
//    private lateinit var database: FirebaseDatabase
//    private lateinit var adminReference:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
//        auth=FirebaseAuth.getInstance()
//        database=FirebaseDatabase.getInstance()
//        adminReference=database.reference.child()

        binding.addandbackprofileadmin.setOnClickListener{
            finish()
        }

        binding.Adminname.isEnabled=false
        binding.Adminemail.isEnabled=false
        binding.Adminaddress.isEnabled=false
        binding.Adminphone.isEnabled=false
        binding.Adminprofilepassword.isEnabled=false

        var isEnable=false
        binding.Adminclicktoedit.setOnClickListener{
            isEnable=!isEnable
            binding.Adminname.isEnabled=isEnable
            binding.Adminemail.isEnabled=isEnable
            binding.Adminaddress.isEnabled=isEnable
            binding.Adminphone.isEnabled=isEnable
            binding.Adminprofilepassword.isEnabled=isEnable

            if (isEnable){
                binding.Adminname.requestFocus()
            }
        }

    }
}