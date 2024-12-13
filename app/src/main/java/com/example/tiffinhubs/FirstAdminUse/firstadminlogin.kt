package com.example.tiffinhubs.FirstAdminUse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ActivityChooserView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tiffinhubs.R
import com.example.tiffinhubs.databinding.ActivityFirstadminloginBinding
import com.example.tiffinhubs.model.adminModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class firstadminlogin : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding: ActivityFirstadminloginBinding by lazy {
        ActivityFirstadminloginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val googleSignInOptions=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        //initalize firebase Auth
        auth= Firebase.auth
        //initalize firebase Auth
        database= Firebase.database.reference
        //initalize google sign in
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)

        binding.AdminLoginbutton.setOnClickListener {
            //grt text from editext
            email=binding.adminEmaileditTextview1.text.toString().trim()
            password=binding.adminpasswordeditTextText.text.toString().trim()

            if (email.isBlank() || password.isBlank()){
                Toast.makeText(this,"Please Fill All Detail", Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email,password)
            }
        }
        binding.admingooglebutton.setOnClickListener{
            val signIntent=googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }
        binding.donthavebuttonAdmin.setOnClickListener {
            val intent = Intent(this, firstadminsignup::class.java)
            startActivity(intent)
        }

    }

    private fun createAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{task->
            if (task.isSuccessful){
                Toast.makeText(this,"Login Successfully",Toast.LENGTH_SHORT).show()
                val admin=auth.currentUser
                updateUI(admin)
            }else{
                Toast.makeText(this,"Please Create Account",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if (result.resultCode== Activity.RESULT_OK){
            val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful){
                val account : GoogleSignInAccount=task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener{authTask ->
                    if (authTask.isSuccessful){
                        //successfull sign in with google
                        Toast.makeText(this,"Successfull sign in with google",Toast.LENGTH_SHORT).show()
                        updateUI(authTask.result?.user)
                        finish()
                    }else{
                        Toast.makeText(this,"google Sign in Failed",Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"google Sign in Failed",Toast.LENGTH_SHORT).show()
            }
        }
    }
//check if user is already logged in
    override fun onStart() {
        super.onStart()
        val currentAdmin=auth.currentUser
        if (currentAdmin != null){
            startActivity(Intent(this,MainActivity2Admin::class.java))
            finish()
        }
    }
    private fun updateUI(admin: FirebaseUser?) {
        startActivity(Intent(this,MainActivity2Admin::class.java))
        finish()
    }
}
