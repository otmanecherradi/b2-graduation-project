package me.otmane.mathresolver.ui.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.otmane.mathresolver.R


class ResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_result, container, false)
        val textView: TextView = view.findViewById(R.id.textViewResult)
        var args  = this.arguments
        val results = args?.get("Results")
        textView.text = results.toString()
        return view
    }

}