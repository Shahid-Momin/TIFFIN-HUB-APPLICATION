package com.example.tiffinhubs.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tiffinhubs.R
import com.example.tiffinhubs.databinding.FragmentProfileBinding
import com.example.tiffinhubs.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding

    private val auth=FirebaseAuth.getInstance()
    private val database=FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentProfileBinding.inflate(inflater,container,false)

        setUserData()

        binding.saveInformationProfile.setOnClickListener{
            val name=binding.name.text.toString()
            val email=binding.email.text.toString()
            val address=binding.address.text.toString()
            val phone=binding.phone.text.toString()

            updateUserData(name,email,address,phone)
        }
        return binding.root
    }

//    private fun updateUserData(name: String, email: String, address: String, phone: String) {
//        val userId=auth.currentUser?.uid
//        if (userId !=null){
//            val userReference=database.getReference("user").child(userId)
//            val userData= hashMapOf(
//                "name" to name,
//                "address" to address,
//                "email" to email,
//                "phone" to phone,
//
//            )
//            userReference.setValue(userData).addOnSuccessListener {
//                Toast.makeText(requireContext(), "Profile Update Successfully", Toast.LENGTH_SHORT).show()
//            }
//                .addOnFailureListener{
//                    Toast.makeText(requireContext(), "Profile Update Failed", Toast.LENGTH_SHORT).show()
//                }
//        }
//    }
private fun updateUserData(name: String, email: String, address: String, phone: String) {
    val userId = auth.currentUser?.uid
    if (userId != null) {
        val userReference = database.getReference("user").child(userId)

        // Fetch existing user data
        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Get existing user profile
                    val existingUserProfile = snapshot.getValue(UserModel::class.java)
                    val password = existingUserProfile?.password // Retain the existing password

                    // Create a new user data map including the password
                    val userData = hashMapOf(
                        "name" to name,
                        "address" to address,
                        "email" to email,
                        "phone" to phone,
                        "password" to password // Include the password here
                    )

                    // Update user data in Firebase
                    userReference.setValue(userData).addOnSuccessListener {
                        Toast.makeText(requireContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), "Profile Update Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error fetching user data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


    private fun setUserData() {
        val userId=auth.currentUser?.uid
        if (userId !=null){
            val userReference=database.getReference("user").child(userId)

            userReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val userProfile=snapshot.getValue(UserModel::class.java)
                        if (userProfile !=null){
                            binding.name.setText(userProfile.name)
                            binding.address.setText(userProfile.address)
                            binding.email.setText(userProfile.email)
                            binding.phone.setText(userProfile.phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

    }


}
