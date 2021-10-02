package com.example.nush_hack21.model

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import java.net.URLEncoder

class ProductSearch(
    context: Context
) {
    val apiKey = "aa6d3ebb5a4b0bccc800f50c42217292152bdc001830594d220b660293230e06d"
    val queue = Volley.newRequestQueue(context)
    val baseurl = "https://serpapi.com/search.json?"


    fun productSearch(query: String, success: (res: SerpapiResponse) -> Unit, error: () -> Unit) {
        val url = "${baseurl}q=${URLEncoder.encode(query)}&tbm=shop&location=Dallas&hl=en&api_key=${apiKey}"
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                // Display the first 500 characters of the response string.
//                textView.text = "Response is: ${response.substring(0, 500)}"
                success(Gson().fromJson(response,SerpapiResponse::class.java))
            },
            { error() })
        queue.add(stringRequest)
    }
}
