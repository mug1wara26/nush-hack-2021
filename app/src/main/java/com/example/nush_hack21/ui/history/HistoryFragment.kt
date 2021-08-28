package com.example.nush_hack21.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nush_hack21.R
import com.example.nush_hack21.model.Product
import com.example.nush_hack21.ui.image.adapters.ProductAdapter
import com.example.nush_hack21.user
import kotlinx.android.synthetic.main.activity_product_info.*


class HistoryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        user.history.sortBy { it.timestamp }
        val products = ArrayList<Product>()
        user.history.forEach {
            products.add(it.product)
        }
        products.reverse()


        val productAdapter = ProductAdapter(products.toArray().toList() as List<Product>, requireContext())

        val historyRecyclerView: RecyclerView = view.findViewById(R.id.recycler_view_history)
        historyRecyclerView.layoutManager = LinearLayoutManager(context)
        historyRecyclerView.adapter = productAdapter

        return view
    }

    companion object {

    }
}