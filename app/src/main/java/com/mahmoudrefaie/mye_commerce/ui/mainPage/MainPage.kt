package com.mahmoudrefaie.mye_commerce.ui.mainPage

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import coil.load
import com.google.android.material.chip.Chip
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.mahmoudrefaie.mye_commerce.ui.CartActivity
import com.mahmoudrefaie.mye_commerce.ui.OfferProduct
import com.mahmoudrefaie.mye_commerce.ui.profile.Profile
import com.mahmoudrefaie.mye_commerce.R
import com.mahmoudrefaie.mye_commerce.pojo.Ads
import com.mahmoudrefaie.mye_commerce.pojo.AdsDatabase
import com.mahmoudrefaie.mye_commerce.ui.Login
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainPage : AppCompatActivity(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener {
    var database: FirebaseDatabase ?= null
    var adsRef: DatabaseReference ?= null

    var adsDatabase : AdsDatabase ?= null
    var fbFirestore : FirebaseFirestore?=null
    //var mAdsList:ArrayList<Ads>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        database = Firebase.database
        adsRef = database?.getReference("ads")

        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.navigation_menu)
        navView.bringToFront()
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        showCart.setOnClickListener(this)

        navView.setNavigationItemSelectedListener(this)

        categoryChipGroup.setOnCheckedChangeListener { group, checkedId ->
            try {
                val chip: Chip = group.findViewById(checkedId)
                chip.setChipBackgroundColorResource(R.color.thirdColor)
                chip.isClickable = false
                val categoryName = chip.text.toString()
                //Open selected category fragment
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                val productsFragment = ProductsFragment(categoryName)
                fragmentTransaction.replace(R.id.fragmentFrame, productsFragment)
                fragmentTransaction.commit()
            }catch (e: Exception){
                e.printStackTrace()
            }
            for (i in 1..group.childCount) {
                if(i != checkedId){
                    val unSelectedChip: Chip = group.findViewById(i)
                    unSelectedChip.setChipBackgroundColorResource(R.color.lightMainColor)
                    unSelectedChip.isClickable = true
                }
            }
        }

    }

    private suspend fun displayAds() {
        val rows = adsDatabase?.adsDao()?.getAds()
        for(row in rows!!){
            when (row.type) {
                "header" -> {
                    headerImage.load(row.imageUrl)
                    //Picasso.get().load(row.imageUrl).placeholder(R.drawable.ic_launcher_background).into(headerImage)
                }
                "left" -> leftImage.load(row.imageUrl)
                "right" -> rightImage.load(row.imageUrl)
                "centerLeft" -> centerLeftImage.load(row.imageUrl)
                "centerRight" -> centerRightImage.load(row.imageUrl)
                "bottomLeft" -> bottomLeftImage.load(row.imageUrl)
                "bottomRight" -> bottomRightImage.load(row.imageUrl)
            }
        }
    }

    override fun onClick(view: View?) {
        if(view?.id == R.id.showCart){
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        adsRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    val adId = i.getValue(Ads::class.java)
                    adsDatabase = AdsDatabase.getInstance(applicationContext)
                    GlobalScope.launch {
                        adsDatabase?.adsDao()?.insertAds(adId!!)

                        displayAds()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))     //To make application not to exit when press back in case Navigation drawable is opened
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_profile -> {
                val intent = Intent(this, Profile::class.java)
                startActivity(intent)
            }
            R.id.nav_offerProduct -> {
                val intent = Intent(this, OfferProduct::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)   // close drawer after selecting

        return  true
    }
}