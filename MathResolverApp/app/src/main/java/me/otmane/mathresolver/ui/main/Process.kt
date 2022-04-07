package me.otmane.mathresolver.ui.main

class Process {

    fun Addition(parameter1: Double,parameter2: Double) : Double {
        return (parameter1 + parameter2)
    }

    fun Subtraction(parameter1: Double,parameter2: Double) = parameter1 - parameter2

    fun Multiplication(parameter1: Double,parameter2: Double) = parameter1 * parameter2

    fun Division(parameter1: Double,parameter2: Double){
        if(parameter2 <= 0)
            print("This parameter must be greater than 0 !")
        else {
            print(parameter1 / parameter2)
        }
    }
}