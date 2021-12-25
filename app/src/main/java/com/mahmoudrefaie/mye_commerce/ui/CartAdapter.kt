package com.mahmoudrefaie.mye_commerce.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.mahmoudrefaie.mye_commerce.R
import com.mahmoudrefaie.mye_commerce.pojo.Cart

class CartAdapter(options: FirestoreRecyclerOptions<Cart>) :
    FirestoreRecyclerAdapter<Cart, CartAdapter.CartHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartHolder(view,parent.context)
    }

    override fun onBindViewHolder(holder: CartHolder, position: Int, model: Cart) {
        val proImage = model.image
        val proName = model.name
        val proCategory = model.category
        val proBrand = model.brand
        val proCondition = model.condition
        val proSeller = model.seller
        val proDescription = model.description
        val proPrice = model.price
        val proDiscount = model.discount
        val proRate = model.rate
        val proColor = model.color
        val proBuyer = model.buyer

        holder.image?.load(proImage)
        holder.name?.text = proName
        holder.price?.text = proPrice.toString() +"  EGP"
    }

    fun deleteFromCart(position: Int){
        snapshots.getSnapshot(position).reference.delete()
    }

    inner class CartHolder(itemView: View,context: Context) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView?= null
        var name: TextView?= null
        var price: TextView?= null
        var shipping: TextView?= null
        var dropdownQTY: AutoCompleteTextView?=null

        init {
            image = itemView.findViewById(R.id.proImage)
            name = itemView.findViewById(R.id.proName)
            price = itemView.findViewById(R.id.proPrice)
            shipping = itemView.findViewById(R.id.shipping)
            dropdownQTY = itemView.findViewById(R.id.dropdownQTYText)
            setQuantities(dropdownQTY!!)

            itemView.setOnClickListener{
                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION && listener != null){
//
//                }
                val intent = Intent(context,ProductDetails::class.java)
                context.startActivity(intent)
            }
        }

    }

    private fun setQuantities(dropdown: AutoCompleteTextView) {
        val qty = arrayOf<Int?>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
        val adapter = ArrayAdapter(dropdown.context, R.layout.dropdown_layout, qty)
        dropdown.setAdapter(adapter)
    }

}