package com.mahmoudrefaie.mye_commerce

import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        setCountries()
        setTermsandPrivacy()

    }

    private fun setCountries(){
        val countries = listOf("Egypt","Soudan","Libya","Tunisia","Algeria","Maroco","Pelastin","Syria")
        val adapter = ArrayAdapter(this,R.layout.dropdown_layout,countries)
        dropdownText.setAdapter(adapter)
    }

    private fun setTermsandPrivacy(){
        val termsAndPrivacyText = "By clicking the \"Sign Up\" button, you confirm that you accept our Terms of use and Privacy Policy"
        val specificText = SpannableString(termsAndPrivacyText)
        val clkspn = object : ClickableSpan(){
            override fun onClick(p0: View) {
                val intent = Intent(applicationContext,TermsPrivacy::class.java)
                startActivity(intent)
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(applicationContext, R.color.thirdColor)
                ds.isUnderlineText = false
                ds.typeface = Typeface.DEFAULT_BOLD
            }
        }
        specificText.setSpan(clkspn,66,97,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        termsAndPrivacy.text = specificText
        termsAndPrivacy.movementMethod = LinkMovementMethod.getInstance()
    }

}