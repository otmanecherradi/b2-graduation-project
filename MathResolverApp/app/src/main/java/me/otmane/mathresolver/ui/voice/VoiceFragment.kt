package me.otmane.mathresolver.ui.voice

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
import me.otmane.mathresolver.databinding.FragmentMicBinding
import me.otmane.mathresolver.repositories.EquationsRepository
import me.otmane.mathresolver.ui.result.ResultFragment


class VoiceFragment : Fragment() {
    private lateinit var binding: FragmentMicBinding
    private lateinit var navController: NavController

    private lateinit var voiceHelper: VoiceHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMicBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this);

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        voiceHelper = VoiceHelper(
            owner = requireActivity(),
            context = requireActivity().applicationContext,
            imageButton = binding.btn,
            loading = binding.loading,
            onResult = ::onResult,
            onError = ::onError,
        )

        voiceHelper.start()
    }

    private fun onResult(result: String) {
        Log.d(TAG, "Result is $result")

        Toast.makeText(context, "Result is $result", Toast.LENGTH_SHORT).show()

        val type = Process.getEquationType(result)

        if (type != null) {
            val b = Bundle()

            val eq = Process.getEquation(result, type)

            EquationsRepository.add(eq)

            b.putSerializable(ResultFragment.EQUATION_ID_ARG, eq.id)

            navController.navigate(R.id.action_navigationScan_to_navigationResult, b)
        }
    }

    private fun onError(e: Int) {
        Log.d(TAG, "Error is $e")

        Toast.makeText(context, "Error is $e", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        voiceHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()

        voiceHelper.stop()
    }

    companion object {
        const val TAG = "VoiceFragment"

        // private const val GOTO_CAMERA_FRAGMENT = R.id.action_main_fragment_to_camera_fragment
    }
}