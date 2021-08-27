package com.example.nush_hack21.ui.notifications

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.nush_hack21.R
import com.example.nush_hack21.databinding.FragmentNotificationsBinding
import kotlinx.android.synthetic.main.fragment_notifications.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class NotificationsFragment : Fragment() {

  private lateinit var notificationsViewModel: NotificationsViewModel
private var _binding: FragmentNotificationsBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!
  private lateinit var outputDirectory: File
  private lateinit var cameraExecutor: ExecutorService
  private var imageCapture: ImageCapture? = null
  private lateinit var progressDialog: ProgressDialog


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

    _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
    val root: View = binding.root

    return root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    if(allPermissionsGranted()){
      startCamera()
    } else{
      this.requestPermissions(
        REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }
    cameraAddBtn.setOnClickListener {
//            Toast.makeText(requireContext(),"Sending image request to server",Toast.LENGTH_SHORT).show()
      cameraAddBtn.isClickable = false
      takePhoto()
    }
    outputDirectory = getOutputDirectory()

    cameraExecutor = Executors.newSingleThreadExecutor()

  }


  override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
  private fun showProgressDialog(){
    progressDialog = ProgressDialog(context)
    with(progressDialog){
      setTitle("Sending request")
      setMessage("Hold on a while")
      setProgressStyle(ProgressDialog.STYLE_SPINNER)
      show()
    }
  }

  private fun takePhoto() {
    showProgressDialog()
    // Get a stable reference of the modifiable image capture use case
    val imageCapture = imageCapture ?: return

    // Create time-stamped output file to hold the image
    val photoFile = File(
      getOutputDirectory(),
      SimpleDateFormat(FILENAME_FORMAT, Locale.US
      ).format(System.currentTimeMillis()) + ".jpg")

    // Create output options object which contains file + metadata
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
    // Set up image capture listener, which is triggered after photo has
    // been taken
    imageCapture.takePicture(
      outputOptions, ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageSavedCallback {
        override fun onError(exc: ImageCaptureException) {
          Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
        }
        @RequiresApi(Build.VERSION_CODES.P)
        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
          val savedUri = Uri.fromFile(photoFile)
          val msg = "Photo capture succeeded: $savedUri"
          Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
          Log.d(TAG, msg)

        }
      })
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
          it.setSurfaceProvider(cameraViewFinder.surfaceProvider)
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