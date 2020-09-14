package com.mahmoudrefaie.mye_commerce.pojo

import android.accounts.AuthenticatorDescription

data class Product(
    val id : String?=null,
    var name : String?=null,
    var category : String?=null,
    var vendorId : String?=null,
    var imagesUrl : ArrayList<String>?=null,
    var description: String?=null,
    var price : Float?=null,
    var discount: Float?=null,
    var rate : Float?=null,
    var color : String?=null,

)