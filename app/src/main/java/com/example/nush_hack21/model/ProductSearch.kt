package com.example.nush_hack21.model

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import java.net.URLEncoder

class ProductSearch(
    context: Context
) {
    val apiKey = "ac6a2bb332f5335ee7a69b9705b567c0933331e9f9c4d11d0f22a81602b88ea8"
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