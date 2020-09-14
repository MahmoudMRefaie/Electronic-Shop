package com.mahmoudrefaie.mye_commerce.ui

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mahmoudrefaie.mye_commerce.R
import com.mahmoudrefaie.mye_commerce.pojo.User
import com.mahmoudrefaie.mye_commerce.ui.mainPage.MainPage
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity(), View.OnClickListener {

    private var fAuth: FirebaseAuth? = null
    var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        fAuth = FirebaseAuth.getInstance()
        user = fAuth?.currentUser

        //BackArrow at ToolBar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_blue_24dp)
        supportActionBar!!.title = ""

        setCountries()  //This function to set countries in Country field dropdown
        setTermsandPrivacy() //This function to make Terms part in TextView is colored and clickable

        signUpBtn.setOnClickListener(this)

    }
    override fun onClick(view: View?) {
        if(view?.id == R.id.signUpBtn) {
            val database = Firebase.database
            val usersRef = database.getReference("users")

            val firstName = firstName.editText?.text.toString()
            val lastName = lastName.editText?.text.toString()
            val email = email.editText?.text.toString().trim()
            val password = password.editText?.text.toString()
            val country = dropdownText.text.toString()

            val selectedGenderId= radioGroup.checkedRadioButtonId
            val selectedGender = findViewById(selectedGenderId) as RadioButton
            val gender = selectedGender.text.toString()

            if(firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && country.isNotEmpty() && gender.isNotEmpty()) {
                fAuth?.createUserWithEmailAndPassword(email,password)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        //Send verification link
                        //user = fAuth?.currentUser!!
                        user?.sendEmailVerification()?.addOnSuccessListener{
                            Toast.makeText(applicationContext, "Email verification has been sent", Toast.LENGTH_LONG).show()
                        }?.addOnFailureListener {
                            Log.d("Email verification ",it.message!!)
                        }

                        val id = usersRef.push().key!!
                        val userData = User(id, firstName, lastName, email, password, gender, country, true)
                        usersRef.child(id).setValue(userData)

                        val intent = Intent(applicationContext, Login::class.java)
                        startActivity(intent)
                        finish()
                    } else
                        Toast.makeText(applicationContext,task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }else
                Toast.makeText(this,"Empty fields", Toast.LENGTH_LONG).show()

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

    private fun setCountries(){
        //val countries = listOf("Egypt","Soudan","Libya","Tunisia","Algeria","Maroco","Pelastin","Syria")
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.country_list,
            R.layout.dropdown_layout
        )
        dropdownText.setAdapter(adapter)
    }

    private fun setTermsandPrivacy(){
        val termsAndPrivacyText = "By clicking the \"Sign Up\" button, you confirm that you accept our Terms of use and Privacy Policy"
        val specificText = SpannableString(termsAndPrivacyText)
        val clkspn = object : ClickableSpan(){
            override fun onClick(p0: View) {
                val intent = Intent(applicationContext, TermsPrivacy::class.java)
                startActivity(intent)
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(applicationContext, R.color.thirdColor)
                ds.isUnderlineText = false
                ds.typeface = Typeface.DEFAULT_BOLD
            }
        }
        specificText.setSpan(clkspn, 66, 97, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        termsAndPrivacy.text = specificText
        termsAndPrivacy.movementMethod = LinkMovementMethod.getInstance()
    }

}