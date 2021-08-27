package com.example.nush_hack21.ui.image.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nush_hack21.R
import com.example.nush_hack21.model.Product

class ProductAdapter(private val items: List<Product>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_product,parent,false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(product: Product) {

        }
    }
}