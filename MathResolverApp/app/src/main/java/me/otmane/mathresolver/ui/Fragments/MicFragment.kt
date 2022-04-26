package me.otmane.mathresolver.ui.Fragments

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import me.otmane.mathresolver.R
import me.otmane.mathresolver.databinding.CameraFragmentBinding
import me.otmane.mathresolver.databinding.MicFragmentBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.sqrt


class MicFragment : Fragment() {

    private var _binding: MicFragmentBinding? = null
    private val binding get() = _binding!!

    private val REQ_CODE_SPEECH_INPUT = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MicFragmentBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this);

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
    private lateinit var navController: NavController

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            REQ_CODE_SPEECH_INPUT -> if (resultCode == Activity.RESULT_OK && null != data) {
                val result: ArrayList<String> = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                val text = result.get(0)
                binding.tvSpeech.setText(text)
                Log.d("TAG",result.get(0))

                //puch the result to result fragment
                val bundle = Bundle()
                bundle.putString("Results", equation2D(text))
                navController.navigate(R.id.action_micFragment_to_resultFragment, bundle)
            }
        }
    }

    fun equation(eq: String) : String {
        val regex = """(\d+.?\d+|\d+)(\+|-|x|×|÷|/)(\d+.?\d+|\d+)""".toRegex()
        val (d1, d2, d3)= regex.find(eq.replace(" ",""))!!.destructured
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

    //--------------------------------------------------------------------
    // FIRST DGREE



    fun equation1D(eq : String) : String{
    val regex = """(\d+.?\d+|\d+)x(\+|-)(\d+.?\d+|\d+)=0""".toRegex()
    var (d1, d2, d3)= regex.find(eq.replace(" ","").replace("moins","-"))!!.destructured
    var result : Double = 0.0
    if(d2 == "+") {
            result = -d3.toDouble() / d1.toDouble()
        }
        else{
            result = d3.toDouble() / d1.toDouble()
        }
        return "Equation : x = $result"
    }


    // SECOND DEGREE


    fun equation2D(eq : String) : String {
        val regex = """(\d+.?\d+|\d+)x\^2(\+|-)(\d+.?\d+|\d+)x(\+|-)(\d+.?\d+|\d+)=0""".toRegex()
        val (A, op1, B, op2, C) = regex.find(eq.replace(" " , "").replace("moins","-"))!!.destructured
        val neweq = eq.replace(" " , "").replace("moins","-").replace("x^2","x²")
        var result1: Double = 0.0
        var result2: Double = 0.0
        var delta: Double = 0.0
        if (op1 == "+" && op2 == "+") {
            delta = (B.toDouble() * B.toDouble()) - 4 * A.toDouble() * C.toDouble()
            if (delta > 0) {
                result1 = (-B.toDouble() + sqrt(delta)) / 2 * A.toDouble()
                result2 = (-B.toDouble() - sqrt(delta)) / 2 * A.toDouble()
            }
        } else if (op1 == "+" && op2 == "-") {
            delta = (B.toDouble() * B.toDouble()) + 4 * A.toDouble() * C.toDouble()

            if (delta > 0) {
                result1 = (-B.toDouble() + sqrt(delta)) / 2 * A.toDouble()
                result2 = (-B.toDouble() - sqrt(delta)) / 2 * A.toDouble()
            }
        } else if (op1 == "-" && op2 == "+") {
            delta = (B.toDouble() * B.toDouble()) - 4 * A.toDouble() * C.toDouble()

            if (delta > 0) {
                result1 = (B.toDouble() + sqrt(delta)) / 2 * A.toDouble()
                result2 = (B.toDouble() - sqrt(delta)) / 2 * A.toDouble()
            }
        } else if (op1 == "-" && op2 == "-") {
            delta = (B.toDouble() * B.toDouble()) + 4 * A.toDouble() * C.toDouble()

            if (delta > 0) {
                result1 = (B.toDouble() + sqrt(delta)) / 2 * A.toDouble()
                result2 = (B.toDouble() - sqrt(delta)) / 2 * A.toDouble()
            }
        }
        if (delta > 0) {
            return "Equation : $neweq . X1 =  $result1  & X2 = $result2"
        } else if (delta == 0.0) {
            result1 = -B.toDouble() / 2 * A.toDouble()
            return "Equation : $eq . X1 =  $result1 ."
        }
        else{
            return "The equation has no solotion."
        }
    }



    //--------------------------------------------------------------------


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}