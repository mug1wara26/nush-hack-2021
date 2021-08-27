package com.example.nush_hack21.ui.notifications

import android.os.AsyncTask
import android.util.Log
import java.net.URL


open class GetBarcodeData(
    private val barcode: String,
    private val listener: AsyncResponse
): AsyncTask<Void, Void, String>() {
    interface AsyncResponse {
        fun processFinish(output: String)
    }

    override fun doInBackground(vararg p0: Void?): String {
        // Send get request to this url https://api.upcitemdb.com/prod/trial/lookup?upc=
//        val url = URL("https://api.upcitemdb.com/prod/trial/lookup?upc=$barcode")
//        val response = url.readText()
//        Log.d("JSONBarcode", response)

        // Return dummy data first as only 100 requests a day
        return "{\"code\":\"OK\",\"total\":1,\"offset\":0,\"items\":[{\"ean\":\"9002490100070\",\"title\":\"NEWEST! Empty can - Red Bull - 250 ml - 2021 - Austria - Alpine Water edition\",\"description\":\"\",\"elid\":\"234115374194\",\"brand\":\"\",\"model\":\"\",\"color\":\"\",\"size\":\"\",\"dimension\":\"\",\"weight\":\"\",\"category\":\"\",\"lowest_recorded_price\":34.99,\"highest_recorded_price\":39.99,\"images\":[],\"offers\":[]}]}"
    }

    override fun onPostExecute(result: String) {
        listener.processFinish(result)
    }
}