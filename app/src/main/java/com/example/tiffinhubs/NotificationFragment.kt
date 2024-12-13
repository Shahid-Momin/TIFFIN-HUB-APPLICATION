package com.example.tiffinhubs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiffinhubs.adapter.NotificationAdapter
import com.example.tiffinhubs.databinding.FragmentNotificationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class NotificationFragment :  BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding= FragmentNotificationBinding.inflate(layoutInflater,container,false)
        val notification= listOf("Your Order has been Canceled Successfully ","Order has been taken by the driver")
        val notificationImages= listOf(R.drawable.sademoji, R.drawable.deliveryboy)
        val adapter= NotificationAdapter(
            ArrayList(notification),
            ArrayList(notificationImages)
        )
        binding.rvNotificationlist.layoutManager= LinearLayoutManager(requireContext())
        binding.rvNotificationlist.adapter=adapter
        return binding.root
    }

    companion object {

    }
}