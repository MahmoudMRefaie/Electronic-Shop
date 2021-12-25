package com.mahmoudrefaie.mye_commerce.ui.mainPage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mahmoudrefaie.mye_commerce.ui.ProductDetails
import com.mahmoudrefaie.mye_commerce.R
import com.mahmoudrefaie.mye_commerce.pojo.Cart
import com.mahmoudrefaie.mye_commerce.pojo.Product

class ProductsFragment(private val categoryName: String) : Fragment() {
    private var recycler: RecyclerView?=null
    private var fbFirestore: FirebaseFirestore?= null
    private var myAdapter: FirestoreRecyclerAdapter<Product, ProductHolder>?=null

    private var emailShared: SharedPreferences? = null
    private var userEmail: String ?= null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        emailShared = activity?.getSharedPreferences("EMAIL", Context.MODE_PRIVATE)
        userEmail = emailShared?.getString("user_email",null)

        fbFirestore = FirebaseFirestore.getInstance()
        recycler = view.findViewById(R.id.productRecycle) as RecyclerView

        //Query
        val query: Query = fbFirestore?.collection("Product")!!
            .whereEqualTo("category", categoryName)
            .orderBy("rate", Query.Direction.DESCENDING)
        //Recycler Options
        val options = FirestoreRecyclerOptions.Builder<Product>().setQuery(query, Product::class.java).build()
        //Recycler adapter
        myAdapter = object : FirestoreRecyclerAdapter<Product, ProductHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
                return ProductHolder(view)
            }

            override fun onBindViewHolder(holder: ProductHolder, position: Int, model: Product) {
                val proImage = model.imagesUrl
                val proName = model.name
                val proCategory = model.category
                val proBrand = model.brand
                val proCondition = model.condition
                val proSeller = model.seller
                val proDescription = model.description
                val proPrice = model.price
                val proDiscount = model.discount
                val proRate = model.rate?.toFloat()
                val proColor = model.color

                holder.image?.load(proImage)
                holder.name?.text = proName
                holder.brand?.text = proBrand
                holder.rate?.rating = proRate!!
                holder.price?.text = proPrice.toString() +"  EGP"
                holder.discount?.text = proDiscount.toString()

                holder.container?.setOnClickListener {
                    val intent = Intent(activity, ProductDetails::class.java)
                    intent.putExtra("image", proImage)
                    intent.putExtra("name", proName)
                    intent.putExtra("category", proCategory)
                    intent.putExtra("brand", proBrand)
                    intent.putExtra("condition", proCondition)
                    intent.putExtra("seller", proSeller)
                    intent.putExtra("description", proDescription)
                    intent.putExtra("price", proPrice!!)
                    intent.putExtra("discount", proDiscount!!)
                    intent.putExtra("rate", proRate)
                    intent.putExtra("color", proColor)
                    startActivity(intent)
                }
                holder.addToCart?.setOnClickListener {
                    val db = FirebaseFirestore.getInstance()
                    val cartRef = db.collection("Cart")
                    val productData = Cart(proImage, proName, proCategory, proBrand, proCondition, proSeller, proDescription, proPrice, proDiscount, proRate, proColor, userEmail)
                    cartRef.add(productData)
                        .addOnSuccessListener {
                            Toast.makeText(activity, "$proName has been added to cart", Toast.LENGTH_LONG).show()
                        }.addOnFailureListener {
                            Toast.makeText(activity, "Not added to cart", Toast.LENGTH_LONG).show()
                        }
                }
            }

        }

        //Recycler View
        showProducts()

        return view
    }

    inner class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var image: ImageView ?= null
        var name: TextView ?= null
        var brand: TextView ?= null
        var rate: RatingBar ?= null
        var price: TextView ?= null
        var discount: TextView ?= null
        var addToCart: TextView ?= null
        var container: ConstraintLayout ?= null

        init {
            image = itemView.findViewById(R.id.productImage)
            name = itemView.findViewById(R.id.productName)
            brand = itemView.findViewById(R.id.productBrand)
            rate = itemView.findViewById(R.id.ratingBar)
            price = itemView.findViewById(R.id.productPrice)
            discount = itemView.findViewById(R.id.productDiscount)
            addToCart = itemView.findViewById(R.id.addToCartTV)
            container = itemView.findViewById(R.id.container)

            discount?.paintFlags = discount?.paintFlags?.or(Paint.STRIKE_THRU_TEXT_FLAG)!!      //line through discount

        }

    }

    private fun showProducts(){
        recycler!!.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = myAdapter
        }
    }

    override fun onStop() {
        super.onStop()
        myAdapter?.stopListening()
    }

    override fun onStart() {
        super.onStart()
        myAdapter?.startListening()
    }
}


