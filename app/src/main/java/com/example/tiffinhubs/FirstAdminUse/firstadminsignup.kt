package com.example.tiffinhubs.FirstAdminUse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tiffinhubs.R
import com.example.tiffinhubs.databinding.ActivityFirstadminsignupBinding
import com.example.tiffinhubs.model.adminModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class firstadminsignup : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database:DatabaseReference
    private lateinit var listoflocation: String
    private lateinit var nameofOwner: String
    private lateinit var nameofRestaruant:String
    private lateinit var email: String
    private lateinit var password: String

    private val binding: ActivityFirstadminsignupBinding by lazy {
        ActivityFirstadminsignupBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //initalize firebase Auth
        auth= Firebase.auth
        //initalize firebase Auth
        database= Firebase.database.reference

        val locationlist = arrayOf("Mumbai", "Bhiwandi", "Thana", "Kalyan")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationlist)
        val autoCompleteTextView = binding.AdminListoflocation
        autoCompleteTextView.setAdapter(adapter)

        binding.AdminAlreadyhaveaccount.setOnClickListener {
            val intent = Intent(this, firstadminlogin::class.java)
            startActivity(intent)
        }

        binding.adminSignupCreateaccount.setOnClickListener {
            //grt text from editext
            listoflocation=binding.AdminListoflocation.text.toString().trim()
            nameofOwner=binding.adminnameofOwner.text.toString().trim()
            nameofRestaruant=binding.adminnameofRestaruant.text.toString().trim()
            email=binding.adminSignupEmail.text.toString().trim()
            password=binding.adminsignupPassword.text.toString().trim()

            if (listoflocation.isBlank() || nameofOwner.isBlank() || nameofRestaruant.isBlank() || email.isBlank() || password.isBlank()){
                Toast.makeText(this,"Please Fill All Detail",Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email,password)
            }

        }

    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{ task ->
            if (task.isSuccessful){
                Toast.makeText(this,"Account create successfully",Toast.LENGTH_SHORT).show()
                saveAdminData()

                val intent = Intent(this, firstadminlogin::class.java)
                startActivity(intent)
                finish()
            }else {
                Toast.makeText(this, "Account create failled", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: failure",task.exception)
            }
        }
    }

    //save data in to database
    private fun saveAdminData() {

        listoflocation=binding.AdminListoflocation.text.toString().trim()
        nameofOwner=binding.adminnameofOwner.text.toString().trim()
        nameofRestaruant=binding.adminnameofRestaruant.text.toString().trim()
        email=binding.adminSignupEmail.text.toString().trim()
        password=binding.adminsignupPassword.text.toString().trim()

        val admin=adminModel(listoflocation,nameofOwner,nameofRestaruant,email, password)
        val adminId= FirebaseAuth.getInstance().currentUser!!.uid

        //save admin data firebase  database
        database.child("admin").child(adminId).setValue(admin)
    }
}