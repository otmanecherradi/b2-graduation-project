package me.otmane.mathresolver.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

enum class EquationType(val displayName: String) {
    Simple("Simple"),
    FirstDegree("First Degree"),
    SecondDegree("Second Degree"),
}


open class Equation() : RealmObject() {
    @PrimaryKey
    var id: UUID = UUID.randomUUID()

    @Required
    var type: String = EquationType.Simple.name
    var typeEnum: EquationType
        get() {
            // because status is actually a String and another client could assign an invalid value,
            // default the status to "Open" if the status is unreadable
            return try {
                EquationType.valueOf(type)
            } catch (e: IllegalArgumentException) {
                EquationType.Simple
            }
        }
        set(value) {
            type = value.name
        }
}