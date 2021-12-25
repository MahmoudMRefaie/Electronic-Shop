package com.mahmoudrefaie.mye_commerce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_checkout.*

class Checkout : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

//        getLocation.setOnClickListener {
//            Toast.makeText(this,"This is my location",Toast.LENGTH_LONG).show()
//        }
    }
}