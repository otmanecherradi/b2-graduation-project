package me.otmane.mathresolver.core

import me.otmane.mathresolver.models.Equation
import me.otmane.mathresolver.models.EquationType

val simpleEquationRegex = """(\d+.?\d+|\d+)([+\-x×÷/])(\d+.?\d+|\d+)""".toRegex()
val firstDegreeEquationRegex = """(\d+.?\d+|\d+)x([+\-])(\d+.?\d+|\d+)=0""".toRegex()
val secondDegreeEquationRegex =
    """(\d+.?\d+|\d+)x\^2([+\-])(\d+.?\d+|\d+)x([+\-])(\d+.?\d+|\d+)=0""".toRegex()

class Process {
    companion object {
        fun getEquation(equationText: String, type: EquationType): Equation {
            val eq = Equation()
            eq.typeEnum = type
            eq.text = equationText

            return eq
        }

        fun checkString(str: String) {
            val type = getEquationType(str)

            if (type == null) {

            }
        }

        fun getEquationType(str: String): EquationType? {
            var type: EquationType? = null

            if (simpleEquationRegex.matches(str)) {
                type = EquationType.Simple
            } else if (firstDegreeEquationRegex.matches(str)) {
                type = EquationType.FirstDegree
            } else if (secondDegreeEquationRegex.matches(str)) {
                type = EquationType.SecondDegree
            }
            return type
        }
    }
}