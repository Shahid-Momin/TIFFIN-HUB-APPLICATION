package com.example.tiffinhubs.FirstAdminUse

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.tiffinhubs.databinding.ActivityAdminAdditemBinding
import com.example.tiffinhubs.model.AllMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class Admin_AdditemActivity : AppCompatActivity() {

    //food item detail
    private lateinit var vendorName:String
    private lateinit var foodName:String
    private lateinit var foodPrice:String
    private lateinit var foodDescription:String
    private lateinit var foodIngredient:String
    private  var foodImageUri: Uri? =null
    //firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database:FirebaseDatabase

    private val binding: ActivityAdminAdditemBinding by lazy {
        ActivityAdminAdditemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //initialize firebase
        auth=FirebaseAuth.getInstance()
        //initialize firebase database instance
        database=FirebaseDatabase.getInstance()

        binding.AddItemButtonAdmin.setOnClickListener{
            //get data from filed
            vendorName = binding.AdminEnterVendorname.text.toString().trim()
            foodName = binding.AdminEnterfoodname.text.toString().trim()
            foodPrice = binding.EnterfoodpriceAdmin.text.toString().trim()
            foodDescription = binding.FoodDescriptionForMenuAdmin.text.toString().trim()
            foodIngredient = binding.FoodIngredentForMenuAdmin.text.toString().trim()

            if (vendorName.isNotBlank() && foodName.isNotBlank() && foodPrice.isNotBlank() && foodDescription.isNotBlank() && foodIngredient.isNotBlank()) {
                uploadData()
                Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Fill All The Details", Toast.LENGTH_SHORT).show()
            }
        }

        binding.SelecteImageForAddMenuAdmin.setOnClickListener{
            pickImage.launch("image/*")
        }

        binding.addandback.setOnClickListener{
            finish()
        }

    }

    private fun uploadData() {
        //get a reference to the "menu" node in the database
        val MenuRef=database.getReference("menu")
        //Generate a unique key for the new menu item
        val newItemKey=MenuRef.push().key

        if (foodImageUri != null){
            val storageRef=FirebaseStorage.getInstance().reference
            val imageRef=storageRef.child("menu_images/${newItemKey}.jpg")
            val uploadTask=imageRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                    downloadUrl->
                    //create a new menu item
                    val newItem=AllMenu(
                        newItemKey,
                        vendorMenu=vendorName,
                        foodName=foodName,
                        foodPrice=foodPrice,
                        foodDescription=foodDescription,
                        foodIngredient=foodIngredient,
                        foodImage = downloadUrl.toString(),
                    )
                    newItemKey?.let {
                        key->
                        MenuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this,"data Uploaded Successfully",Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener() {
                                Toast.makeText(this,"data Uploaded failed",Toast.LENGTH_SHORT).show()
                            }
                    }
                }

            }
                .addOnSuccessListener {
                    Toast.makeText(this,"image Uploaded Successfully",Toast.LENGTH_SHORT).show()
                }

        }else {
            Toast.makeText(this,"image Uploaded failed",Toast.LENGTH_SHORT).show()
        }
    }

   private val pickImage=registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        if (uri != null){
            binding.selectedimage.setImageURI(uri)
            foodImageUri=uri
        }
    }
}