package com.example.nush_hack21.ui.image.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.nush_hack21.R
import com.example.nush_hack21.model.GreenGrade
import com.example.nush_hack21.model.Product
import com.example.nush_hack21.ui.image.ProductInfoActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_product.view.*

class ProductAdapter(private val items: List<Product>, val context: Context) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_product,parent,false),context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(private val view: View, val context: Context) : RecyclerView.ViewHolder(view) {
        fun bind(product: Product) {
            view.productTitle.text = product.title
            Picasso.get().load(product.imageUrl).into(view.productImg)

            view.setOnClickListener {
                val intent = Intent(context,ProductInfoActivity::class.java).apply{
                    putExtra("product",product)
                }
                startActivity(context,intent,null)
            }
            if (product.points != -1){
                bindScoreView(view,product)
            }
        }
        fun bindScoreView(view: View, product: Product) {
            view.productScoreView.visibility = VISIBLE
            view.productScoreView.score = product.points
            val greenGrade = GreenGrade.gradeScore(product.points)
            view.productScoreView.setSecondaryColor(Color.parseColor(GreenGrade.greenGradeColors[greenGrade]))
            view.productScoreView.setTextColor(Color.parseColor(GreenGrade.greenGradeColors[greenGrade]))
        }
    }
}