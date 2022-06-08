package me.otmane.mathresolver.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import me.otmane.mathresolver.R
import me.otmane.mathresolver.databinding.ElementEquationBinding
import me.otmane.mathresolver.models.Equation
import me.otmane.mathresolver.ui.result.ResultFragment


class EquationAdapter(
    private val equationList: ArrayList<Equation>,
    private val navController: NavController
) :
    RecyclerView.Adapter<EquationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ElementEquationBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup, false
        )

        return ViewHolder(binding, navController)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val equation = equationList[position]

        viewHolder.populateDate(equation)
        viewHolder.addListeners(equation)
    }

    override fun getItemCount() = equationList.size

    class ViewHolder(
        private val binding: ElementEquationBinding,
        private val navController: NavController
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateDate(equation: Equation) {
            binding.text.text = equation.text
        }

        fun addListeners(equation: Equation) {
            binding.root.setOnClickListener {
                val b = Bundle()
                b.putSerializable(ResultFragment.EQUATION_ID_ARG, equation.id)
                navController.navigate(R.id.action_navigationHome_to_navigationResult, b)
            }
        }
    }
}
