package com.example.tiffinhubs.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel

import com.example.tiffinhubs.Vendor_menu01


import com.example.tiffinhubs.adapter.Carrieraddapter

import com.example.tiffinhubs.databinding.FragmentHomeBinding
import com.example.tiffinhubs.model.VendorModel

import com.example.tiffinhubs.view_menu_for_home1
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<VendorModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Retrieve and display vendors
        retrieveAndDisplayVendorCarrier()
        return binding.root
    }

    private fun retrieveAndDisplayVendorCarrier() {
        // Get reference to the database
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("vendor")
        menuItems = mutableListOf()

        // Retrieve menu item from the database
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (vendorSnapshot in snapshot.children) {
                    val vendorItem = vendorSnapshot.getValue(VendorModel::class.java)
                    vendorItem?.let { menuItems.add(it) }
                }
                // Display vendor carrier
                randomVendorCarrier()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error retrieving data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun randomVendorCarrier() {
        // Create a shuffled carrier list of vendors
        val shuffledVendors = menuItems.shuffled().take(10)
        setVendorCarrierAdapter(shuffledVendors)
    }

    private fun setVendorCarrierAdapter(subsetVendors: List<VendorModel>) {
        val adapter = Carrieraddapter(subsetVendors) { position ->
            // Handle item click
            val selectedVendor = subsetVendors[position]
            val intent = Intent(requireContext(),Vendor_menu01::class.java).apply {
                putExtra("VENDOR_NAME", selectedVendor.vendorName)
                putExtra("VENDOR_IMAGE", selectedVendor.vendorImage)
                putExtra("VENDOR_ADDRESS", selectedVendor.vendorAddress)
                putExtra("VENDOR_TYPES_OF_FOOD", selectedVendor.vendorTypesOfFood)
            }
            startActivity(intent)
        }

        binding.rvTiffinCarrier.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTiffinCarrier.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>().apply {
            add(SlideModel("https://static.wixstatic.com/media/f82157_002fcf99c0c24d3393957a62386648ea~mv2.png", ScaleTypes.FIT))
            add(SlideModel("https://img.freepik.com/premium-vector/food-ads-promotional-web-banner-template-design_1033790-516.jpg", ScaleTypes.FIT))
            add(SlideModel("https://graphicsfamily.com/wp-content/uploads/edd/2022/02/Free-Food-Advertising-Banner-Template.jpg", ScaleTypes.FIT))
            add(SlideModel("https://loveincorporated.blob.core.windows.net/contentimages/gallery/352faeb5-95f6-41e6-af4c-24ac02b46511-fried-chicken.jpg", ScaleTypes.FIT))
        }

        binding.imageSlider.setImageList(imageList)
        binding.imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
                // Handle double click if necessary
            }

            override fun onItemSelected(position: Int) {
                val itemMessage = "Selected image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })

        binding.ViewMenuHomeFragment.setOnClickListener {
            val intent = Intent(requireContext(), view_menu_for_home1::class.java)
            startActivity(intent)
        }
    }
}
