package com.example.nush_hack21.ui.image

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.nush_hack21.R
import com.example.nush_hack21.model.Product
import com.example.nush_hack21.model.ProductSearch
import com.example.nush_hack21.model.Record
import com.example.nush_hack21.user
import com.google.gson.Gson
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.android.synthetic.main.image_fragment.*
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ImageFragment : Fragment() {


    private lateinit var viewModel: ImageViewModel

    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    val items = mutableListOf<Product>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.image_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(allPermissionsGranted()){
            startCamera()
        } else{
            this.requestPermissions(
                REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        cameraCaptureBtn.setOnClickListener {
            Toast.makeText(requireContext(),"Processing image",Toast.LENGTH_SHORT).show()
            cameraCaptureBtn.isClickable = false
            takePhoto()
        }
        showListItem.setOnClickListener {
            val tmp = arrayListOf<Product>()
            tmp.addAll(items)
            Log.i("showlistitem",tmp.toString())
            val intent = Intent(context,ItemActivity::class.java).apply {
                putParcelableArrayListExtra("items", tmp)
            }
            startActivity(intent)
        }
        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()

    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else activity?.filesDir!!
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return
        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(ContextCompat.getMainExecutor(context), object: ImageCapture.OnImageCapturedCallback() {
            @SuppressLint("UnsafeOptInUsageError")
            override fun onCaptureSuccess(imageProxy: ImageProxy) {
                //get bitmap from image
                val mediaImage = imageProxy.image
                if (mediaImage != null) {
                    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                    scanBarcode(image, imageProxy)
                }
                super.onCaptureSuccess(imageProxy)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
            }
        })
    }

    private fun scanBarcode(image: InputImage, imageProxy: ImageProxy) {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_EAN_13)
            .build()

        val scanner = BarcodeScanning.getClient()

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                // Task completed successfully
                if (barcodes.size == 0)
                    Toast.makeText(context, "No barcode detected, make sure you have adequate lighting and image is focused", Toast.LENGTH_LONG).show()
                Toast.makeText(context,"${barcodes.size} barcodes scanned",Toast.LENGTH_SHORT).show()
                for (barcode in barcodes) {
//                    Toast.makeText(context, "Barcode scanned", Toast.LENGTH_SHORT).show()
                    getBarcodeData(barcode)
                }
            }
            .addOnFailureListener {
                Log.e("BarcodeScanFailure", it.stackTraceToString())
                Toast.makeText(context, "Error occurred, unable to scan", Toast.LENGTH_LONG).show()
            }
            .addOnCompleteListener {
                // It's important to close the imageProxy
                imageProxy.close()
            }

        cameraCaptureBtn.isClickable = true
    }

    // Impt function, gives us title of product
    private fun getBarcodeData(barcode: Barcode) {
        val value = barcode.rawValue!!
        Log.d("BarcodeValue", value)
        // Send get request to this url https://api.upcitemdb.com/prod/trial/lookup?upc=
        GetJson("https://api.upcitemdb.com/prod/trial/lookup?upc=$value", object: GetJson.AsyncResponse {
            override fun processFinish(response: String) {
                // This code is fucking ugly but im too lazy to import klaxon and deal with null safety shit
                // Code will not break as long as api response format does not change
                // total denotes if there is data on this barcode
                val total = response.substring(response.indexOf("\"total\":")).substring(8, 9).toInt()
                if (total != 0) {
                    // Remove all characters before title
                    val titleStart = response.substring(response.indexOf("\"title\":\"")).substring(9)
                    val title = titleStart.substring(0, titleStart.indexOf('"'))

                    ProductSearch(requireContext()).productSearch(title, {res ->
                        Log.i("productsearch",res.toString())
                        if (res.shopping_results.isNotEmpty()) {
                            val product = Product(res.shopping_results[0].title,res.shopping_results[0].thumbnail)
                            val record = Record(System.currentTimeMillis()/1000, product)
                            items.add(product)

                            user.history.add(record)
                            val json = Gson().toJson(record)
                        }
                    },{})

//                    items.add(Product(title))
                    Log.d("title", title)
                }
                else Log.d("title", "no data on product")
            }
        }).execute()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder()
                .build()
            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(context,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
//        Navigation.findNavController(requireView()).popBackStack()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

}

open class GetJson(
    private val url: String,
    private val listener: AsyncResponse
): AsyncTask<Void, Void, String>() {
    interface AsyncResponse {
        fun processFinish(response: String)
    }

    override fun doInBackground(vararg p0: Void?): String {
        Log.d("here", "here")
        var response = ""
        with(URL(url).openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // optional default is GET

            Log.d("http get", "\nSent 'GET' request to URL : $url; Response Code : $responseCode")

            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    response += line + "\n"
                }
            }
        }
        Log.d("JSON", response)

        return response
    }

    override fun onPostExecute(result: String) {
        listener.processFinish(result)
    }
}