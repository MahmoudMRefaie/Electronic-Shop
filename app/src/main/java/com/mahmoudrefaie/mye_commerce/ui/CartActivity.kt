package com.mahmoudrefaie.mye_commerce.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.mahmoudrefaie.mye_commerce.R
import com.mahmoudrefaie.mye_commerce.pojo.Cart
import kotlinx.android.synthetic.main.activity_cart.*


class CartActivity : AppCompatActivity() {
    private var fbFirestore: FirebaseFirestore?= null
    private var cartRef: CollectionReference?= null
    private var adapter : CartAdapter ?=null

    private var emailShared: SharedPreferences? = null
    private var userEmail: String ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        emailShared = getSharedPreferences("EMAIL", Context.MODE_PRIVATE)
        userEmail = emailShared?.getString("user_email", null)

        fbFirestore = FirebaseFirestore.getInstance()
        cartRef = fbFirestore?.collection("Cart")

        //Recycler View
        setUpShowCartRecycler()

        total.text = calculateTotal().toString()

        shoppingTV.text = "Shopping Cart (${countDocuments()})"

        checkout.setOnClickListener {
            val intent = Intent(this,checkout::class.java)
            startActivity(intent)
        }

    }

    private fun setUpShowCartRecycler() {
        //Query
        val query: Query = cartRef!!.whereEqualTo("buyer", userEmail)

        //Recycler Options
        val options = FirestoreRecyclerOptions.Builder<Cart>().setQuery(query, Cart::class.java).build()

        adapter = CartAdapter(options)

        cartRecycler.setHasFixedSize(true)
        cartRecycler.layoutManager = LinearLayoutManager(this)
        cartRecycler.adapter = adapter

        //Delete product from Cart
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter?.deleteFromCart(viewHolder.adapterPosition)
                adapter?.itemCount
            }
        }).attachToRecyclerView(cartRecycler)
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        if (adapter != null) {
            adapter?.stopListening()
        }
    }

    private fun countDocuments(): Int {
        var count = 0
        val myDocs = cartRef?.get()!!
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful)
                    for (document in task.result!!)
                        if (document.getString("buyer") == userEmail)
                            count++
                        else
                    count = 0
            })
        return count
    }

    private fun calculateTotal(): Double {
        var total = 0.0
        val myDocs = cartRef?.get()?.addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful)
                for (document in task.result!!) {
                    if (document.getString("buyer") == userEmail) {
                        val productPrice = document.getDouble("price")
                        total += productPrice!!
                    }
                }
            else
                Toast.makeText(this,"isn't Successful",Toast.LENGTH_LONG).show()
        })
        return total
    }

}