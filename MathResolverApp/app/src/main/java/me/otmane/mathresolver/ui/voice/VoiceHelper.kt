package me.otmane.mathresolver.ui.voice

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.material.progressindicator.CircularProgressIndicator
import java.util.*


class VoiceHelper(
    private val owner: FragmentActivity,
    private val context: Context,
    private val imageButton: ImageButton,
    private val loading: CircularProgressIndicator,
    private val onResult: (result: String) -> Unit,
    private val onError: (e: Int) -> Unit,
) {
    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(owner);

    fun start() {
        if (allPermissionsGranted()) {
            startMic()
        } else {
            ActivityCompat.requestPermissions(owner, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    fun stop() {
        speechRecognizer.destroy();
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun startMic() {
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(getSpeechRecognitionListener(onResult, onError))

        imageButton.setOnClickListener {
            speechRecognizer.startListening(speechRecognizerIntent)
        }
    }

    private fun getSpeechRecognitionListener(
        onResult: (result: String) -> Unit,
        onError: (e: Int) -> Unit
    ): SpeechRecognitionListener {
        return SpeechRecognitionListener(
            onResult,
            onError,
            loading
        )
    }

    private class SpeechRecognitionListener(
        private val onResult: (result: String) -> Unit,
        private val onErr: (e: Int) -> Unit,
        private val loading: CircularProgressIndicator,
    ) : RecognitionListener {
        override fun onReadyForSpeech(p0: Bundle?) {
            TODO("Not yet implemented")
        }

        override fun onBeginningOfSpeech() {
            loading.visibility = View.VISIBLE
        }

        override fun onRmsChanged(p0: Float) {
            TODO("Not yet implemented")
        }

        override fun onBufferReceived(p0: ByteArray?) {
            TODO("Not yet implemented")
        }

        override fun onEndOfSpeech() {
            TODO("Not yet implemented")
        }

        override fun onError(error: Int) {
            onErr(error)
        }

        override fun onResults(bundle: Bundle?) {
            loading.visibility = View.GONE

            val data = bundle!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            onResult(data!![0])
        }

        override fun onPartialResults(p0: Bundle?) {
            TODO("Not yet implemented")
        }

        override fun onEvent(p0: Int, p1: Bundle?) {
            TODO("Not yet implemented")
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startMic()
            } else {
                Toast.makeText(
                    context,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    context, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    companion object {
        const val REQUEST_CODE_PERMISSIONS = 42
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.RECORD_AUDIO)
    }

}
