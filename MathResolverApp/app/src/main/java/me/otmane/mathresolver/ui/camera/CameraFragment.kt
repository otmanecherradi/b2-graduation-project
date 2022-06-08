package me.otmane.mathresolver.ui.camera

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import me.otmane.mathresolver.R
import me.otmane.mathresolver.core.Process
import me.otmane.mathresolver.databinding.FragmentCameraBinding
import me.otmane.mathresolver.repositories.EquationsRepository
import me.otmane.mathresolver.ui.result.ResultFragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CameraFragment : Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private lateinit var cameraHelper: CameraHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this);

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        cameraHelper = CameraHelper(
            owner = requireActivity(),
            context = requireActivity().applicationContext,
            viewFinder = binding.cameraPreview,
            onResult = ::onResult,
            onError = ::onError,
        )

        cameraHelper.start()
    }

    private fun onResult(result: String) {
        Log.d(TAG, "Result is $result")

        Toast.makeText(context, "Result is $result", Toast.LENGTH_SHORT).show()

        val type = Process.getEquationType(result)

        if (type != null) {
            val b = Bundle()

            val eq = Process.getEquation(result, type)

            EquationsRepository.add(eq)

            b.putSerializable(ResultFragment.EQUATION_ARG, eq)

            navController.navigate(R.id.action_navigationScan_to_navigationResult, b)
        }
    }

    private fun onError(e: Exception) {
        Log.d(TAG, "Error is ${e.message}")

        Toast.makeText(context, "Error is ${e.message}", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        cameraHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

//    private fun startCamera() {
//        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
//
//        cameraProviderFuture.addListener({
//            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//            val preview = Preview.Builder()
//                .build()
//                .also {
//                    it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
//                }
//
//            imageCapture = ImageCapture.Builder().build()
//
//            try {
//                cameraProvider.unbindAll()
//                cameraProvider.bindToLifecycle(
//                    this, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture
//                )
//            } catch (exc: Exception) {
//                Log.e(TAG, "Use case binding failed", exc)
//            }
//        }, ContextCompat.getMainExecutor(requireContext()))
//    }
//
//    private fun takePhoto() {
//        val imageCapture = imageCapture ?: return
//
//        val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
//
//        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
//            .build()
//
//        imageCapture.takePicture(
//            outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
//                override fun onError(exc: ImageCaptureException) {
//                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
//                }
//
//                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
//                    val msg = "Photo capture succeeded: ${output.savedUri}"
//                    Log.d(TAG, msg)
//
//                    val image = InputImage.fromFilePath(requireContext(), output.savedUri!!)
//
//                    recognizeText(image)
//
//                }
//            }
//        )
//    }
//
//    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
//        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
//    }
//
//    @Deprecated("Deprecated in Java")
//    override fun onRequestPermissionsResult(
//        requestCode: Int, permissions: Array<String>, grantResults:
//        IntArray
//    ) {
//        if (requestCode == REQUEST_CODE_PERMISSIONS) {
//            if (allPermissionsGranted()) {
//                startCamera()
//            } else {
//                Toast.makeText(
//                    requireContext(),
//                    "Permissions not granted by the user.",
//                    Toast.LENGTH_SHORT
//                ).show()
//                navController.navigateUp()
//            }
//        }
//    }
//
//    private fun recognizeText(image: InputImage) {
//        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
//
//        val result = recognizer.process(image)
//            .addOnSuccessListener { visionText ->
//                processTextBlock(visionText)
//            }
//            .addOnFailureListener { e ->
//                Log.e(TAG, e.toString())
//                Toast.makeText(requireActivity(), e.toString(), Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    private fun processTextBlock(result: Text) {
//        val resultText = result.text
//        Log.d(TAG, "resultText $resultText")
//        Toast.makeText(requireActivity(), "resultText $resultText", Toast.LENGTH_SHORT).show()
//
//        for (block in result.textBlocks) {
//            val blockText = block.text
//            val blockCornerPoints = block.cornerPoints
//            val blockFrame = block.boundingBox
//
//            Log.d(TAG, "blockText $blockText")
//            Log.d(TAG, "blockCornerPoints ${blockCornerPoints.toString()}")
//            Log.d(TAG, "blockFrame ${blockFrame.toString()}")
//
//            Toast.makeText(requireActivity(), "blockText $blockText", Toast.LENGTH_SHORT).show()
//            Toast.makeText(requireActivity(), "blockCornerPoints ${blockCornerPoints.toString()}", Toast.LENGTH_SHORT).show()
//            Toast.makeText(requireActivity(), "blockFrame ${blockFrame.toString()}", Toast.LENGTH_SHORT).show()
//
////            for (line in block.lines) {
////                val lineText = line.text
////                val lineCornerPoints = line.cornerPoints
////                val lineFrame = line.boundingBox
////
////                for (element in line.elements) {
////                    val elementText = element.text
////                    val elementCornerPoints = element.cornerPoints
////                    val elementFrame = element.boundingBox
////
////                    Log.d(TAG, elementText)
////                    Log.d(TAG, elementCornerPoints.toString())
////                    Log.d(TAG, elementFrame.toString())
////                }
////            }
//        }
//    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
        cameraHelper.stop()
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