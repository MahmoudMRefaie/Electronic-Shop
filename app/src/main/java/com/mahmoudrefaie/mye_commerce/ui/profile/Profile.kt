package com.mahmoudrefaie.mye_commerce.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.mahmoudrefaie.mye_commerce.ui.profile.FavoriteFragment
import com.mahmoudrefaie.mye_commerce.ui.profile.InfoFragment
import com.mahmoudrefaie.mye_commerce.R
import com.mahmoudrefaie.mye_commerce.pojo.User
import kotlinx.android.synthetic.main.activity_offer_product.*
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.IOException
import java.util.*

class Profile : AppCompatActivity() {
    private var fragment: Fragment?= null
    private var mStorage: StorageReference?= null

    private var emailShared: SharedPreferences? = null
    private var userEmail: String ?= null

    private var profileImageUrl: String ?= null
    private val db = FirebaseFirestore.getInstance()
    private val productRef = db.collection("User")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        mStorage = FirebaseStorage.getInstance().reference

        emailShared = getSharedPreferences("EMAIL", Context.MODE_PRIVATE)
        userEmail = emailShared?.getString("user_email",null)

        //BackArrow at ToolBar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_blue_24dp)
        supportActionBar!!.title = ""

        profilePic.setOnClickListener {
            //Check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                    //Permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show pop up to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    //permission already granted
                    pickImageFromGallery(IMAGE_PICK_CODE)
                }
            } else {
                //System OS < Marshmallow
                pickImageFromGallery(IMAGE_PICK_CODE)
            }
        }

        chipNavigationBar.setItemSelected(R.id.info,true)
        supportFragmentManager.beginTransaction().replace(R.id.navigationContainer, InfoFragment()).commit()
        chipNavigationBar.setOnItemSelectedListener {
            when(it){
                R.id.info -> fragment = InfoFragment()
                R.id.favorites -> fragment = FavoriteFragment()
            }
            if(fragment != null){
                supportFragmentManager.beginTransaction().replace(R.id.navigationContainer,fragment!!).commit()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun pickImageFromGallery(selectedImageStatic : Int) {
        //Intent to pick image
        val gallery = Intent(Intent.ACTION_OPEN_DOCUMENT)
        gallery.addCategory(Intent.CATEGORY_OPENABLE)
        gallery.type = "image/*"
        startActivityForResult(gallery,selectedImageStatic)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null && data.data != null) {
            try {
                val imageUri = data.data
                profilePic.setImageURI(imageUri)
                storeImages(imageUri!!)
                val user = User(profileImageUrl)
            } catch (e: IOException) {
                e.message
            }
        }
    }

    private fun storeImages(imageUri: Uri){
        val storageDB = Firebase.storage
        val imageRef = storageDB.reference
        val productImages = imageRef.child(userEmail!!).child("ProfilePicture").child(Calendar.getInstance().time.toString())
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
                profileImageUrl = downloadUri.toString()
            } else {
                //Handle failures
            }
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }
}