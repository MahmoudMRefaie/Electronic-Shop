package com.mahmoudrefaie.mye_commerce.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseUser
import com.mahmoudrefaie.mye_commerce.R


class EmailVerificationDialog(private val getContext: Context,private val user: FirebaseUser?) : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.email_verification_dialog, null)

        builder.setView(view)
            .setTitle("Email Verification")
            .setNegativeButton("cancel") { _, _ -> }
            .setPositiveButton("Ok") { _, _ -> sendVerification(user!!) }
        return builder.create()
    }

    private fun sendVerification(user: FirebaseUser) {
        user.sendEmailVerification().addOnSuccessListener{
            Toast.makeText(getContext, "Email verification has been sent", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Log.d("Email verification ", it.message!!)
        }
    }

}
