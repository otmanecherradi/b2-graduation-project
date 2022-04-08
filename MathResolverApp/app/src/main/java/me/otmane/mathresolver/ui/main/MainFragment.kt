package me.otmane.mathresolver.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import me.otmane.mathresolver.R
import me.otmane.mathresolver.databinding.MainFragmentBinding

import android.widget.Button
import me.otmane.mathresolver.CameraFragment


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    class MainFragment : Fragment() {
        private var _binding: MainFragmentBinding? = null
        private val binding get() = _binding!!

        private lateinit var navController: NavController

        private lateinit var viewModel: MainViewModel

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = MainFragmentBinding.inflate(inflater, container, false)
            navController = NavHostFragment.findNavController(this);

            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            viewModel = ViewModelProvider(this)[MainViewModel::class.java]


            binding.btnCamera.setOnClickListener {
                navController.navigate(GOTO_CAMERA_FRAGMENT)

                //--------- set button to show CameraFragment ---------
                val OpenCamera: Button = binding.root.findViewById(R.id.btn_camera) as Button
                OpenCamera.setOnClickListener {
                    requireActivity()
                        .supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.cameraView, CameraFragment())
                        .commit()
                    /*val fragment = CameraFragment() //navigate to camera fragment
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.cameraView, fragment)
            transaction?.commit()*/

                }
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

        companion object {
            const val TAG = "MenuFragment"
            fun newInstance() = MainFragment()

            private const val GOTO_CAMERA_FRAGMENT = R.id.CameraFragment
        }
    }
}
