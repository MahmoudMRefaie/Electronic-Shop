package com.mahmoudrefaie.mye_commerce.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mahmoudrefaie.mye_commerce.R
import com.mahmoudrefaie.mye_commerce.pojo.Product
import kotlinx.android.synthetic.main.activity_offer_product.*
import java.util.*
import kotlin.collections.ArrayList

class OfferProduct : AppCompatActivity(), View.OnClickListener {
    private var images :ArrayList<Uri?>? = null
    //private var imagesUrl :ArrayList<Uri?>? = null
    private var position = 0
    private var imageUrl: String ?= null
    private var emailShared: SharedPreferences? = null
    private var userEmail: String ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_product)

        images = ArrayList()
        //imagesUrl = ArrayList()

        emailShared = getSharedPreferences("EMAIL", Context.MODE_PRIVATE)
        userEmail = emailShared?.getString("user_email",null)

        showImages.setFactory { ImageView(applicationContext) }

        setCategories()

        pickImages.setOnClickListener(this)
        previousSwitcher.setOnClickListener(this)
        nextSwitcher.setOnClickListener(this)
        offer.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.pickImages){
            pickImages()
        }else if(v?.id == R.id.previousSwitcher){
            if(position > 0){
                position--
                showImages.setImageURI(images!![position])
            }else{
                Toast.makeText(this, "No more previous images", Toast.LENGTH_LONG).show()
            }
        }else if(v?.id == R.id.nextSwitcher) {
            if (position < images!!.size - 1) {
                position++
                showImages.setImageURI(images!![position])
            } else {
                Toast.makeText(this, "No more next images", Toast.LENGTH_LONG).show()
            }
        }else if (v?.id == R.id.offer) {
            addProduct()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_CODE && data != null) {
            if (data.clipData != null){
                //Pick multi image
//                images?.clear()
//                val count = data.clipData?.itemCount
//                for(i in 0 until count!!){
//                    val imageUri = data.clipData?.getItemAt(i)?.uri
//                    images?.add(imageUri)
//                }
//                showImages.setImageURI(images!![0])
//                position = 0
            }else{
                //Pick single image
                images?.clear()
                val imageUri = data.data
                showImages.setImageURI(imageUri)
                storeImages(imageUri!!)
                //images?.add(imageUri)
                position = 0
            }

        }
    }

    private fun pickImages(){
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image(s)"), PICK_IMAGE_CODE)
    }

    private fun addProduct() {
        val db = FirebaseFirestore.getInstance()
        val productRef = db.collection("Product")

        //val getImages = images
        val getName = name.editText?.text.toString()
        val getCategory = dropdownCategories.text.toString()
        val getCondition = condition.editText?.text.toString()
        val getBrand= brand.editText?.text.toString()
        var getPrice: Float ?= null
        if(price.editText?.text.toString().isNotEmpty()) getPrice = price.editText?.text.toString().toFloat()
        var getDiscount: Float ?=null
        if(discount.editText?.text.toString().isNotEmpty()) getDiscount = discount.editText?.text.toString().toFloat()
        val getColor = color.editText?.text.toString()
        val getDescription = description.editText?.text.toString()

//        if (images?.size!! > 0)
//            for (i in 0 until images?.size!!)
//                storeImages(images!![i]!!,i.toString())

        if (getName.isNotEmpty() && getCategory.isNotEmpty() && getBrand.isNotEmpty() && getCondition.isNotEmpty() && userEmail != null && imageUrl != null && getDescription.isNotEmpty() && getColor.isNotEmpty()) {
            val product = Product(getName, getCategory, getBrand, getCondition, userEmail, imageUrl, getDescription, getPrice, getDiscount?.plus(getPrice!!),0.0, getColor)
            productRef.add(product)
                .addOnSuccessListener {
                    Toast.makeText(this, "your product has been added", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Not added", Toast.LENGTH_LONG).show()
                }
        }else{Toast.makeText(this, "Empty fields", Toast.LENGTH_LONG).show()}

    }

    private fun storeImages(imageUri: Uri){
        val storageDB = Firebase.storage
        val imageRef = storageDB.reference
        val productImages = imageRef.child("Products").child(userEmail!!).child(Calendar.getInstance().time.toString())
        val uploadTask = productImages.putFile(imageUri)
        //to get image url in storage
        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            productImages.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                imageUrl = downloadUri.toString()
            } else {
                //Handle failures
            }
        }
    }

    private fun setCategories(){
        //val countries = listOf("Egypt","Sudan","Libya","Tunisia","Algeria","Maroco","Pelastin","Syria")
        val adapter = ArrayAdapter.createFromResource(this,
            R.array.category_list,
            R.layout.dropdown_layout
        )
        dropdownCategories.setAdapter(adapter)
    }

    companion object{
        private const val PICK_IMAGE_CODE = 1000
    }
}