package com.example.nush_hack21.ui.image

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nush_hack21.R
import com.example.nush_hack21.model.Product
import com.example.nush_hack21.ui.image.adapters.ProductAdapter
import kotlinx.android.synthetic.main.activity_item.*

class ItemActivity : AppCompatActivity() {
    private lateinit var products : List<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        products = intent.getParcelableArrayListExtra("items") ?: listOf()
        Log.i("ItemActivity",products.toString())

        productList.layoutManager = LinearLayoutManager(this)
        val adapter = ProductAdapter(products,this)
        productList.adapter = adapter
    }

}