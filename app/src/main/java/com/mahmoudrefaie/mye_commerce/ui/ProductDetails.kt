package com.mahmoudrefaie.mye_commerce.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.firebase.firestore.FirebaseFirestore
import com.mahmoudrefaie.mye_commerce.R
import com.mahmoudrefaie.mye_commerce.pojo.Cart
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetails : AppCompatActivity() {
    private var emailShared: SharedPreferences? = null
    private var userEmail: String ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        emailShared = getSharedPreferences("EMAIL", Context.MODE_PRIVATE)
        userEmail = emailShared?.getString("user_email",null)

        showProductDetails()

        addToCartBtn.setOnClickListener {
            addToCart()
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showProductDetails() {
        val image = intent.getStringExtra("image")
        val name = intent.getStringExtra("name")
        val category = intent.getStringExtra("category")
        val brand = intent.getStringExtra("brand")
        val condition = intent.getStringExtra("condition")
        val seller = intent.getStringExtra("seller")
        val description = intent.getStringExtra("description")
        val price = intent.getFloatExtra("price",0.0f)
        val discount = intent.getFloatExtra("discount",0.0f)
        val rate = intent.getFloatExtra("rate",0.0f)
        val color = intent.getStringExtra("color")

        imageView.load(image)
        proName.text = name
        ratingBar.rating = rate
        proPrice.text = price.toString()
        proDiscount.text = discount.toString()
        proDiscount?.paintFlags = proDiscount?.paintFlags?.or(Paint.STRIKE_THRU_TEXT_FLAG)!!
        proCondition.text = condition
        proBrand.text = brand
        proColor.text = color
        proDescription.text = description

    }

    private fun addToCart() {
        val db = FirebaseFirestore.getInstance()
        val cartRef = db.collection("Cart")

        val image = intent.getStringExtra("image")
        val name = intent.getStringExtra("name")
        val category = intent.getStringExtra("category")
        val brand = intent.getStringExtra("brand")
        val condition = intent.getStringExtra("condition")
        val seller = intent.getStringExtra("seller")
        val description = intent.getStringExtra("description")
        val price = intent.getFloatExtra("price",0.0f)
        val discount = intent.getFloatExtra("discount",0.0f)
        val rate = intent.getFloatExtra("rate",0.0f)
        val color = intent.getStringExtra("color")

        if (image?.isNotEmpty()!! && name?.isNotEmpty()!! && category?.isNotEmpty()!! && brand?.isNotEmpty()!! && condition?.isNotEmpty()!! && seller?.isNotEmpty()!! && description?.isNotEmpty()!! && price != null && discount!=null && rate!=null && color?.isNotEmpty()!!) {
            val productData = Cart(image, name, category, brand, condition, seller, description, price, discount, rate, color,userEmail)
            cartRef.add(productData)
                .addOnSuccessListener {

                }.addOnFailureListener {
                    Toast.makeText(this, "Not added to cart", Toast.LENGTH_LONG).show()
                }
        }else{
            Toast.makeText(this, "Empty fields", Toast.LENGTH_LONG).show()}

    }

}