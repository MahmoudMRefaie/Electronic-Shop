package com.mahmoudrefaie.mye_commerce.pojo

data class User (
    val id : String?=null,
    var firstName : String?=null,
    var lastName : String?=null,
    var email : String?=null,
    var password : String?=null,
    var gender : String?=null,
    var country : String?=null,
    var verified: Boolean?=null
)