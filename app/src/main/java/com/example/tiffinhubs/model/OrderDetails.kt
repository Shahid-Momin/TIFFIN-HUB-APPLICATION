package com.example.tiffinhubs.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.util.ArrayList

class OrderDetails():Serializable {
    var userId:String?=null
    var userName:String?=null
    var foodNames:MutableList<String>?=null
    var foodImages:MutableList<String>?=null
    var foodPrices:MutableList<String>?=null
    var foodQuantities:MutableList<Int>?=null
    var address:String?=null
    var totalPrice:String?=null
    var phoneNumber:String?=null
    var orderAccepted:Boolean?=false
    var paymentReceived:Boolean?=false
    var itemPushKey:String?=null
    var currentTime:Long=0

    constructor(parcel: Parcel) : this() {
        userId = parcel.readString()
        userName = parcel.readString()
        address = parcel.readString()
        totalPrice = parcel.readString()
        phoneNumber = parcel.readString()
        orderAccepted = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        paymentReceived = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }

    constructor(
        userId: String,
        name: String,
        foodItemName: ArrayList<String>,
        foodItemPrice: ArrayList<String>,
        foodItemImage: ArrayList<String>,
        foodItemQuantities: ArrayList<Int>,
        address: String,
        totalAmount:String,
        phone: String,
        time: Long,
        itemPushKey: String?,
        b: Boolean,
        b1: Boolean,
    ) : this(){
        this.userId=userId
        this.userName=name
        this.foodNames=foodItemName
        this.foodPrices=foodItemPrice
        this.foodImages=foodItemImage
        this.foodQuantities=foodItemQuantities
        this.address=address
        this.totalPrice=totalAmount
        this.phoneNumber=phone
        this.currentTime=time
        this.itemPushKey=itemPushKey
        this.orderAccepted=orderAccepted
        this.paymentReceived=paymentReceived
    }

     fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(userName)
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNumber)
        parcel.writeValue(orderAccepted)
        parcel.writeValue(paymentReceived)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }

     fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }
}