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
        binding.btnCamera
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.btnCamera.setOnClickListener {
            navController.navigate(GOTO_CAMERA_FRAGMENT)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "MenuFragment"
        fun newInstance() = MainFragment()

        private const val GOTO_CAMERA_FRAGMENT = R.id.action_main_fragment_to_camera_fragment
    }
}