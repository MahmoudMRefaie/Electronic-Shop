package com.mahmoudrefaie.mye_commerce.pojo

data class User (
    val id : String?=null,
    var firstName : String?=null,
    var lastName : String?=null,
    var email : String?=null,
    var password : String?=null,
    var gender : String?=null,
    var city : String?=null
){
    constructor(id: String,firstName : String,lastName : String, pic: String, email : String, password : String, gender : String,
                city : String) : this(id,firstName,lastName, email, password, gender, city)
}