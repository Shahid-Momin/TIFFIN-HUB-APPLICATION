package com.example.tiffinhubs.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiffinhub.adapter.MenuAdapter
import com.example.tiffinhubs.adapter.SearchAdapter

import com.example.tiffinhubs.databinding.FragmentSearchBinding
import com.example.tiffinhubs.model.AllMenu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter // Change type to MenuAdapter
    private lateinit var database: FirebaseDatabase
    private val originalMenuItems = mutableListOf<AllMenu>() // Use proper name

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        // Retrieve menu from the database
        retrieveMenuItem()
        // Setup search view
        setupSearchView()

        return binding.root
    }

    private fun retrieveMenuItem() {
        // Get database reference
        database = FirebaseDatabase.getInstance()
        val foodReference: DatabaseReference = database.reference.child("menu")
        foodReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(AllMenu::class.java) // Changed to AllMenu
                    menuItem?.let {
                        originalMenuItems.add(it)
                    }
                }
                showAllMenu()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error (optional)
            }
        })
    }

    private fun showAllMenu() {
        val filteredMenuItems = ArrayList(originalMenuItems)
        setAdapter(filteredMenuItems)
    }

    private fun setAdapter(filteredMenuItems: List<AllMenu>) { // Ensure the type is AllMenu
        adapter = MenuAdapter(filteredMenuItems, requireContext())
        binding.rvViewMenu.layoutManager = LinearLayoutManager(requireContext()) // Corrected
        binding.rvViewMenu.adapter = adapter
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { filterMenuItem(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterMenuItem(it) }
                return true
            }
        })
    }

    private fun filterMenuItem(query: String) {
        val filteredMenuItems = originalMenuItems.filter {
            it.foodName?.contains(query, ignoreCase = true) == true // Change foodName based on your model
        }
        setAdapter(filteredMenuItems)
    }
}