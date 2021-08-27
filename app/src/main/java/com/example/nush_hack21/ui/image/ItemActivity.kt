package com.example.nush_hack21.ui.image

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.nush_hack21.R
import com.example.nush_hack21.model.Product
import com.example.nush_hack21.ui.image.adapters.ProductAdapter
import kotlinx.android.synthetic.main.activity_item.*

class ItemActivity : AppCompatActivity() {
    val products = listOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        productList.layoutManager = LinearLayoutManager(this)
        val adapter = ProductAdapter(products)
        productList.adapter = adapter

    }



}