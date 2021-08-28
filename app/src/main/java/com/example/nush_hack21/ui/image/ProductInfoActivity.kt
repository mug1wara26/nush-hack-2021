package com.example.nush_hack21.ui.image

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nush_hack21.R
import com.example.nush_hack21.model.Product

class ProductInfoActivity : AppCompatActivity() {
    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_info)

        product = intent.getParcelableExtra<Product>("product")!!

    }
}