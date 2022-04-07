package me.otmane.mathresolver.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import me.otmane.mathresolver.CameraFragment
import me.otmane.mathresolver.R
import me.otmane.mathresolver.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)

        //--------- set button to show CameraFragment ---------
        val btn_camera : Button = binding.root.findViewById(R.id.btn_camera)
        btn_camera.setOnClickListener {
            val fragment = CameraFragment() //navigate to camera fragment
            val transaction = fragmentManager?.beginTransaction()
                ?.replace(R.id.camera_view, fragment)
                ?.commit()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}