package me.otmane.mathresolver.core

import me.otmane.mathresolver.models.Equation
import me.otmane.mathresolver.models.EquationType

val simpleEquationRegex = """(\d+.?\d+|\d+)([+\-*x×÷/])(\d+.?\d+|\d+)""".toRegex()
val firstDegreeEquationRegex = """(\d+.?\d+|\d+)x([+\-])(\d+.?\d+|\d+)=0""".toRegex()
val secondDegreeEquationRegex =
    """(\d+.?\d+|\d+)x\^2([+\-])(\d+.?\d+|\d+)x([+\-])(\d+.?\d+|\d+)=0""".toRegex()

class Process {
    companion object {
        private fun cleanString(str: String): String {
            return str.replace(" ", "").trim()
        }

        fun getEquation(equationText: String, type: EquationType): Equation {
            val eq = Equation()
            eq.typeEnum = type
            eq.text = cleanString(equationText)

            return eq
        }

        fun checkString(str: String) {
            val type = getEquationType(str)

            if (type == null) {

            }
        }

        fun getEquationType(str: String): EquationType? {
            var type: EquationType? = null

            val equationText = cleanString(str)

            if (simpleEquationRegex.matches(equationText)) {
                type = EquationType.Simple
            } else if (firstDegreeEquationRegex.matches(equationText)) {
                type = EquationType.FirstDegree
            } else if (secondDegreeEquationRegex.matches(equationText)) {
                type = EquationType.SecondDegree
            }
            return type
        }

        fun calculateResult(equation: Equation): String {
            var result = 0.0

            if (equation.typeEnum == EquationType.Simple) {
                val equationText = cleanString(equation.text)
                val (nbr1, operation, nbr2) = simpleEquationRegex.find(equationText)!!.destructured

                when (operation) {
                    "+" -> result = nbr1.toDouble() + nbr2.toDouble()
                    "-" -> result = nbr1.toDouble() - nbr2.toDouble()
                    "×" -> result = nbr1.toDouble() * nbr2.toDouble()
                    "x" -> result = nbr1.toDouble() * nbr2.toDouble()
                    "*" -> result = nbr1.toDouble() * nbr2.toDouble()
                    "/" -> result = nbr1.toDouble() / nbr2.toDouble()
                    "÷" -> result = nbr1.toDouble() / nbr2.toDouble()
                }
            }
            return "$result"
        }
    }
}