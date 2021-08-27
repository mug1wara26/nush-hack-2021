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
        val url = URL("https://api.upcitemdb.com/prod/trial/lookup?upc=$barcode")
        val response = url.readText()
        Log.d("JSONBarcode", response)
        return response
    }

    override fun onPostExecute(result: String) {
        listener.processFinish(result)
    }
}