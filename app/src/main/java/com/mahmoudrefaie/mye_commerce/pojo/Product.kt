package com.mahmoudrefaie.mye_commerce.pojo

import android.net.Uri
import com.google.gson.annotations.Expose

data class Product(
    var name: String?=null,
    var category: String?=null,
    var brand: String?=null,
    var condition: String?=null,
    var seller : String?=null,
    var imagesUrl : String?=null,
    var description: String?=null,
    var price: Float?=null,
    var discount: Float?=null,
    var rate: Double?=null,
    var color: String?=null,
    //var favourite : Boolean?=null,
)