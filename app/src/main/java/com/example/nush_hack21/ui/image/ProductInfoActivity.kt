package com.example.nush_hack21.ui.image

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.nush_hack21.R
import com.example.nush_hack21.model.GreenGrade
import com.example.nush_hack21.model.Product
import com.example.nush_hack21.model.ProductSearch
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_info.*
import kotlinx.android.synthetic.main.card_product.view.*

class ProductInfoActivity : AppCompatActivity() {
    private lateinit var product: Product
    private lateinit var alternatives: MutableList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_info)

        product = intent.getParcelableExtra<Product>("product")!!

        productDesc.text = product.title
        Picasso.get().load(product.imageUrl).into(productPoster)
        if(product.points != -1)
            bindScoreView(product)

    }

    fun bindScoreView(product: Product) {
        productScoreView.visibility = View.VISIBLE
        productScoreView.score = product.points
        val greenGrade = GreenGrade.gradeScore(product.points)
        productScoreView.setSecondaryColor(Color.parseColor(GreenGrade.greenGradeColors[greenGrade]))
        productScoreView.setTextColor(Color.parseColor(GreenGrade.greenGradeColors[greenGrade]))
    }

    fun populateAlternatives() {
//        ProductSearch(this).productSearch(product.title, { res ->
//            if (res.shopping_results.isNotEmpty()) {
//                val altOptions = res.shopping_results.map { Product(it.title, it.thumbnail) }
//            }
//        },{})


    }
}