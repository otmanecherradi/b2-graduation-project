package me.otmane.mathresolver.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import me.otmane.mathresolver.core.LiveRealmObject
import me.otmane.mathresolver.models.Equation
import me.otmane.mathresolver.repositories.EquationsRepository

class EquationViewModel : ViewModel() {
    private val _equations: LiveRealmObject<Equation> = LiveRealmObject(null)
    val equations: LiveData<Equation>
        get() = _equations

    fun add(eq: Equation) {
        EquationsRepository.add(eq)
    }

    override fun onCleared() {
        EquationsRepository.onClear();
    }
}