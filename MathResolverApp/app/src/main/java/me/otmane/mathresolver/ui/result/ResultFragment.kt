package me.otmane.mathresolver.ui.result

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import me.otmane.mathresolver.databinding.FragmentResultBinding
import me.otmane.mathresolver.models.Equation
import me.otmane.mathresolver.repositories.EquationsRepository
import org.bson.types.ObjectId


class ResultFragment : Fragment() {
    private lateinit var binding: FragmentResultBinding
    private lateinit var navController: NavController

    private lateinit var equation: Equation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            equation = requireArguments().getSerializable(EQUATION_ARG) as Equation
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this);

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: $equation")

    }

    companion object {
        const val TAG = "ResultFragment"

        const val EQUATION_ARG = "EQUATION";

        // private const val GOTO_CAMERA_FRAGMENT = R.id.action_main_fragment_to_camera_fragment
    }
}