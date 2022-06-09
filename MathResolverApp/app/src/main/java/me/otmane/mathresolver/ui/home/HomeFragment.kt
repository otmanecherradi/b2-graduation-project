package me.otmane.mathresolver.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import me.otmane.mathresolver.adapters.EquationAdapter
import me.otmane.mathresolver.databinding.FragmentHomeBinding
import me.otmane.mathresolver.models.Equation
import me.otmane.mathresolver.repositories.EquationsRepository
import me.otmane.mathresolver.ui.result.EquationViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController

    private val equationViewModel: EquationViewModel by viewModels()

    val equationList = ArrayList<Equation>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        val result = EquationsRepository.getAll()
        result?.addChangeListener { results ->
            results.forEach {
                Log.d("data001", "$it")
                if (!equationList.contains(it))
                    equationList.add(it)
            }
            binding.equations.adapter?.notifyDataSetChanged()

        }
    }

    private fun initRecyclerView() {
        val equationAdapter = EquationAdapter(equationList, navController)

        binding.equations.apply {
            adapter = equationAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    requireActivity(),
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    companion object {
        const val TAG = "HomeFragment"
    }
}