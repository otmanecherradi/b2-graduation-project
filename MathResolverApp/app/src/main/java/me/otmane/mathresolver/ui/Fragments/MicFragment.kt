package me.otmane.mathresolver.ui.Fragments

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import me.otmane.mathresolver.R
import me.otmane.mathresolver.databinding.CameraFragmentBinding
import me.otmane.mathresolver.databinding.MicFragmentBinding
import java.util.*
import kotlin.collections.ArrayList


class MicFragment : Fragment() {

    private var _binding: MicFragmentBinding? = null
    private val binding get() = _binding!!

    private val REQ_CODE_SPEECH_INPUT = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MicFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSpeech.setOnClickListener {
            speechInput()
        }
    }

    /*
    * Google Speech Input prompt (Voice)
    * */

    fun speechInput() {

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_alert))

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (a: ActivityNotFoundException) {
            Toast.makeText(requireContext(),
                getString(R.string.not_supported),
                Toast.LENGTH_SHORT).show()
        }
    }

    /*
    * Displaying the dialog input (Words)
    * */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            REQ_CODE_SPEECH_INPUT -> if (resultCode == Activity.RESULT_OK && null != data) {
                val result: ArrayList<String> = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                binding.tvSpeech.setText(result.get(0))

                val bundle = Bundle()
                bundle.putString("Results", equation(result.get(0)))
                val fragment = ResultFragment()
                fragment.arguments = bundle
                fragmentManager?.beginTransaction()?.replace(R.id.main_nav_container, fragment)?.commit()
            }
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

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}