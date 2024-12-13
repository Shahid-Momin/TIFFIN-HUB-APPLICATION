package com.example.tiffinhubs.FirstAdminUse

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tiffinhubs.R
import com.example.tiffinhubs.databinding.ActivityAdminCreateNewVendorBinding
import com.example.tiffinhubs.model.VendorModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class Admin_CreateNewVendorActivity : AppCompatActivity() {

    //vendor  detail
    private lateinit var vendorName:String
    private lateinit var vendorFoodType:String
    private lateinit var vendorAddress:String
    private  var vendorImageUri: Uri? =null

    //firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase


    private val binding:ActivityAdminCreateNewVendorBinding by lazy {
        ActivityAdminCreateNewVendorBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //initialize firebase
        auth=FirebaseAuth.getInstance()
        //initialize firebase database instance
        database=FirebaseDatabase.getInstance()

        binding.AdminCreateNewVendorButton.setOnClickListener{
            //get data from filed
            vendorName=binding.AdminCreateNewVendorName.text.toString().trim()
            vendorFoodType=binding.AdminCreateNewVendorTypesOfFood.text.toString().trim()
            vendorAddress=binding.AdminCreateNewVendorAddress.text.toString().trim()

            if (vendorName.isNotBlank() || vendorFoodType.isNotBlank() || vendorAddress.isNotBlank()){
                uploadVendorData()
                Toast.makeText(this, "Vendor Added Successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Fill All The Details", Toast.LENGTH_SHORT).show()
            }
        }

        binding.AdminCreateNewVendorImage.setOnClickListener{
            pickImage.launch("image/*")
        }



        binding.addandbacknewuser.setOnClickListener{
            finish()
        }

    }

    private fun uploadVendorData() {

        //get a reference to the "menu" node in the database
        val MenuRef=database.getReference("vendor")
        //Generate a unique key for the new menu item
        val newItemKey=MenuRef.push().key

        if (vendorImageUri !=null){
            val storageRef= FirebaseStorage.getInstance().reference
            val imageRef=storageRef.child("menu_images/${newItemKey}.jpg")
            val uploadTask=imageRef.putFile(vendorImageUri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                    downloadUrl->
                    //create new vendor
                    val newVendor=VendorModel(
                        vendorName=vendorName,
                        vendorTypesOfFood = vendorFoodType,
                        vendorAddress = vendorAddress,
                        vendorImage = downloadUrl.toString(),

                    )
                    newItemKey?.let {
                        key->
                        MenuRef.child(key).setValue(newVendor).addOnSuccessListener {
                            Toast.makeText(this,"Vendor data uploaded susscessfully",Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener(){
                                Toast.makeText(this, "Vendor data failed", Toast.LENGTH_SHORT).show()
                            }
                    }

                }
            }
                .addOnSuccessListener {
                    Toast.makeText(this, "Vendor Image Uploaded Successfully", Toast.LENGTH_SHORT).show()
                }

        }else{
            Toast.makeText(this, "Vendor Image Uploaded Failed", Toast.LENGTH_SHORT).show()
        }

    }

    private val pickImage=registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        if (uri != null){
           binding.AdminCreateVendorImage.setImageURI(uri)
            vendorImageUri=uri
        }
    }
}