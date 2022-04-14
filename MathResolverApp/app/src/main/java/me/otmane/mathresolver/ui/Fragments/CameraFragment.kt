package me.otmane.mathresolver.ui.Fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import me.otmane.mathresolver.MainActivity
import me.otmane.mathresolver.R
import me.otmane.mathresolver.databinding.CameraFragmentBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraFragment : Fragment() {
    private var _binding: CameraFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CameraFragmentBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this);

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        outputDirectory = MainActivity.getOutputDirectory(requireContext())

        binding.takePictureBtn.setOnClickListener {
            takePhoto()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
            .build()

        imageCapture.takePicture(
            outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Log.d(TAG, msg)

                    val image = InputImage.fromFilePath(requireContext(), output.savedUri!!)

                    recognizeText(image)

                }
            }
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                navController.navigateUp()
            }
        }
    }

    private fun recognizeText(image: InputImage) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
               // processTextBlock(visionText)
                val text = visionText.text

                // equation(text)
                val bundle = Bundle()
                bundle.putString("Results", equation(text))
                val fragment = ResultFragment()
                fragment.arguments = bundle
                fragmentManager?.beginTransaction()?.replace(R.id.main_nav_container, fragment)?.commit()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, e.toString())
                Toast.makeText(requireActivity(), e.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    fun equation(eq: String) : String {
        val regex = """(\d+.?\d+)(\+|-|x|×|÷|/)(\d+.?\d+)""".toRegex()
        val (d1, d2, d3) = regex.find(eq)!!.destructured
        var result : Double = 0.0
        if(d2 == "+") {
            result = d1.toDouble() + d3.toDouble()
        }
        else if(d2 == "-"){
            result = d1.toDouble() - d3.toDouble()
        }
        else if(d2 == "×" || d2 == "x"){
            result = d1.toDouble() * d3.toDouble()
        }
        else if(d2 == "/" || d2 == "÷" && d3!= "0"){
            result = d1.toDouble() / d3.toDouble()
        }
        return "Equation : $d1 $d2 $d3 = $result"
    }

    private fun processTextBlock(result: Text) {
        val resultText = result.text
        Log.d(TAG, "resultText $resultText")
        Toast.makeText(requireActivity(), "resultText $resultText", Toast.LENGTH_SHORT).show()

        for (block in result.textBlocks) {
            val blockText = block.text
            val blockCornerPoints = block.cornerPoints
            val blockFrame = block.boundingBox

            Log.d(TAG, "blockText $blockText")
            Log.d(TAG, "blockCornerPoints ${blockCornerPoints.toString()}")
            Log.d(TAG, "blockFrame ${blockFrame.toString()}")

            Toast.makeText(requireActivity(), "blockText $blockText", Toast.LENGTH_SHORT).show()
            Toast.makeText(requireActivity(), "blockCornerPoints ${blockCornerPoints.toString()}", Toast.LENGTH_SHORT).show()
            Toast.makeText(requireActivity(), "blockFrame ${blockFrame.toString()}", Toast.LENGTH_SHORT).show()

//            for (line in block.lines) {
//                val lineText = line.text
//                val lineCornerPoints = line.cornerPoints
//                val lineFrame = line.boundingBox
//
//                for (element in line.elements) {
//                    val elementText = element.text
//                    val elementCornerPoints = element.cornerPoints
//                    val elementFrame = element.boundingBox
//
//                    Log.d(TAG, elementText)
//                    Log.d(TAG, elementCornerPoints.toString())
//                    Log.d(TAG, elementFrame.toString())
//                }
//            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()

        _binding = null
    }

    companion object {
        const val TAG = "CameraFragment"
        fun newInstance() = CameraFragment()

        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf(Manifest.permission.CAMERA).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()

        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private fun createFile(baseFolder: File, format: String, extension: String) =
            File(
                baseFolder, SimpleDateFormat(format, Locale.US)
                    .format(System.currentTimeMillis()) + extension
            )
    }
}