package com.example.nush_hack21.ui.image

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nush_hack21.R
import com.example.nush_hack21.model.Product
import com.example.nush_hack21.model.ProductSearch
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_info.*

class ProductInfoActivity : AppCompatActivity() {
    private lateinit var product: Product
    private lateinit var alternatives: MutableList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_info)

        product = intent.getParcelableExtra<Product>("product")!!

        productDesc.text = product.title
        Picasso.get().load(product.imageUrl).into(productPoster)
    }

    fun populateAlternatives() {
        ProductSearch(this).productSearch(product.title, { res ->
            if (res.shopping_results.isNotEmpty()) {
                val altOptions = res.shopping_results.map { Product(it.title, it.thumbnail) }
            }
        },{})

    }
}