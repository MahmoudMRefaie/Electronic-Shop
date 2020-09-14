package com.mahmoudrefaie.mye_commerce.ui.mainPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mahmoudrefaie.mye_commerce.R

class ProductsFragment(private val categoryName: String) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_category,container,false)
        var intro = view.findViewById(R.id.intro) as TextView
        intro.text = categoryName
        return view
    }
}