package com.mahmoudrefaie.mye_commerce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() , View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference()

        signUp.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        if(view?.id == R.id.signUp){
            val intent = Intent(this,SignUp::class.java)
            startActivity(intent)
        }
    }
}