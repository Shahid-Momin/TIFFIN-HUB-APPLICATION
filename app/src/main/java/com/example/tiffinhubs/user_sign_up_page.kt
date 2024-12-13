package com.example.tiffinhubs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tiffinhubs.databinding.ActivityUserSignUpPageBinding
import com.example.tiffinhubs.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class user_sign_up_page : AppCompatActivity() {
    private lateinit var username:String
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding:ActivityUserSignUpPageBinding by lazy {
        ActivityUserSignUpPageBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val googleSignInOptions= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        //initialize Firebase auth
        auth= Firebase.auth
        //initialize Firebase database
        database=Firebase.database.reference
        //initialize firebase google
        googleSignInClient= GoogleSignIn.getClient(this,googleSignInOptions)

        binding.createSignInAccountButtonUser.setOnClickListener{
            username=binding.userNameSignIn.text.toString()
            email=binding.userEmailSignIn.text.toString().trim()
            password=binding.usersPasswordSignIn.text.toString().trim()

            if ( username.isBlank()|| email.isBlank() || password.isBlank() ){
                Toast.makeText(this,"Please Fill All The Details",Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email,password)
                Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()

            }
        }

        binding.alreadyhaveaccount.setOnClickListener{
            val intent= Intent(this,user_login_page::class.java)
            startActivity(intent)
        }
        binding.userGoogleSignInButton.setOnClickListener{
            val signIntent=googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }
    }
    //launcher for google signin
    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if (result.resultCode== Activity.RESULT_OK){

            val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful){
                val account:GoogleSignInAccount?=task.result
                val credential=GoogleAuthProvider.getCredential(account?.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener{task->
                    if (task.isSuccessful){
                        Toast.makeText(this, "Sign in successfully", Toast.LENGTH_SHORT).show()
                        // save data from database
                        //saveUser
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

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
            task ->
            if (task.isSuccessful){
                Toast.makeText(this,"Account Created Successfully",Toast.LENGTH_SHORT).show()
                saveUserData()
                startActivity(Intent(this,user_login_page::class.java))
                finish()
                Log.d("Account", "createAccount: account createf",task.exception)
            }else{
                Toast.makeText(this,"Account Created Failed",Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Failure",task.exception)
            }
        }
    }

    private fun saveUserData() {
        //retrieve data from input filed
        username=binding.userNameSignIn.text.toString()
        email=binding.userEmailSignIn.text.toString().trim()
        password=binding.usersPasswordSignIn.text.toString().trim()

        val user=UserModel(username,email,password)
        val userId= FirebaseAuth.getInstance().currentUser!!.uid
        //save data to firebase data base
        database.child("user").child(userId).setValue(user)
    }
}