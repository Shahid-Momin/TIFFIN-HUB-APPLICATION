package com.example.tiffinhubs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.tiffinhubs.databinding.ActivityUserLoginPageBinding
import com.example.tiffinhubs.model.UserModel
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

class user_login_page : AppCompatActivity() {
   // private  var userName:String?=null
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var auth: FirebaseAuth
    private lateinit var database:DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding:ActivityUserLoginPageBinding by lazy {
        ActivityUserLoginPageBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val googleSignInOptions= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        //initialize firebase auth
        auth= Firebase.auth
        //initialize firebase database
        database=Firebase.database.reference
        //initialize google sign in
        googleSignInClient= GoogleSignIn.getClient(this,googleSignInOptions)

        //login email and password

        binding.userAccountloginButton.setOnClickListener {
            //get data from text field
            email=binding.userEmailLogin.text.toString().trim()
            password=binding.userEmailPasswordLogin.text.toString().trim()

            if (email.isBlank() || password.isBlank()){
                Toast.makeText(this, "Please Enter All Details", Toast.LENGTH_SHORT).show()
            }else{
                createUser(email,password)
                Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
            }

        }
        binding.donthavebutton.setOnClickListener {
            val intent = Intent(this, user_sign_up_page::class.java)
            startActivity(intent)
        }

        //google signin
        binding.googleButtonLogin.setOnClickListener{
            val signIntent=googleSignInClient.signInIntent
            launcher.launch(signIntent)


        }
    }
    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if (result.resultCode== Activity.RESULT_OK){

            val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful){
                val account:GoogleSignInAccount?=task.result
                val credential=GoogleAuthProvider.getCredential(account?.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener{task->
                    if (task.isSuccessful){
                        Toast.makeText(this, "Sign in successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this, "Sign In Field", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }else{
            Toast.makeText(this, "Sign In Field", Toast.LENGTH_SHORT).show()
        }


    }

    private fun createUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{task->
            if (task.isSuccessful){
                val user=auth.currentUser
                updateUi(user)
            }else{
//                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task ->
//                    if (task.isSuccessful){
//                        saveUserdata()
//                        val user=auth.currentUser
//                        updateUi(user)
//                    }else{
//                        Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
//                    }
//                }
                Toast.makeText(this, "create account", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun saveUserdata() {
//        //get data from text field
//
//        email=binding.userEmailLogin.text.toString().trim()
//        password=binding.userEmailPasswordLogin.text.toString().trim()
//
//        val user=UserModel(userName,email,password)
//        val userId=FirebaseAuth.getInstance().currentUser!!.uid
//        //save data into database
//        database.child("user").child(userId).setValue(user)
//
//
//    }

    override fun onStart() {
        super.onStart()
        val currentUser=auth.currentUser
        if (currentUser != null){
            val intent = Intent(this, choose_location::class.java)
            startActivity(intent)
            finish()

        }
    }
    private fun updateUi(user: FirebaseUser?) {
        val intent = Intent(this, choose_location::class.java)
        startActivity(intent)
        finish()

    }
}