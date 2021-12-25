package com.mahmoudrefaie.mye_commerce.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mahmoudrefaie.mye_commerce.R
import com.mahmoudrefaie.mye_commerce.ui.mainPage.MainPage
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.email
import kotlinx.android.synthetic.main.activity_login.password

class Login : AppCompatActivity() , View.OnClickListener {

    var fAuth: FirebaseAuth? = null
    var user: FirebaseUser? = null
    private var emailShared: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        fAuth = FirebaseAuth.getInstance()
        emailShared = getSharedPreferences("EMAIL",MODE_PRIVATE);
        // Write a message to the database
        val database = Firebase.database
        val myRef = database.reference
        user = fAuth?.currentUser

        if(user != null && user?.isEmailVerified!!){
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
            finish()
        }

        signUptv.setOnClickListener(this)
        signIn.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        if(view?.id == R.id.signIn) {

            val email = email.editText?.text.toString().trim()
            val password = password.editText?.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                fAuth?.signInWithEmailAndPassword(email, password)!!.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (fAuth?.currentUser?.isEmailVerified!!) {
                            val userEmail = fAuth?.currentUser!!.email
                            emailShared?.edit()?.putString("user_email",userEmail)?.apply();
                            val intent = Intent(applicationContext, MainPage::class.java)
                            startActivity(intent)
                        }else{
                            openEmailVerificationDialog()
                        }
                    } else
                            Toast.makeText(applicationContext, task.exception?.message, Toast.LENGTH_LONG).show()
                }

            }else
                Toast.makeText(this,"empty fields", Toast.LENGTH_LONG).show()

        }
        else if(view?.id == R.id.signUptv){
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }

    private fun openEmailVerificationDialog() {
        val emailVerificationDialog = EmailVerificationDialog(this,user)
        val sfm = this.supportFragmentManager.beginTransaction()
        emailVerificationDialog.show(sfm, "Email Verification Dialog")
    }
}