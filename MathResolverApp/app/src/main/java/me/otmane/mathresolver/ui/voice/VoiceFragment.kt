package me.otmane.mathresolver.ui.voice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import me.otmane.mathresolver.databinding.MainFragmentBinding


class VoiceFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding
    private lateinit var navController: NavController

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this);

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCamera.setOnClickListener {
           //  navController.navigate(GOTO_CAMERA_FRAGMENT)
        }
    }

    companion object {
        const val TAG = "MenuFragment"
        fun newInstance() = VoiceFragment()

        // private const val GOTO_CAMERA_FRAGMENT = R.id.action_main_fragment_to_camera_fragment
    }
}