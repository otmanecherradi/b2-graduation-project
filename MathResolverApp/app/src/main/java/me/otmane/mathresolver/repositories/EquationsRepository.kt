package me.otmane.mathresolver.repositories

import io.realm.Realm
import io.realm.RealmResults
import me.otmane.mathresolver.models.Equation
import org.bson.types.ObjectId

class EquationsRepository {

    companion object {
        private val realm by lazy { Realm.getDefaultInstance() }

        fun add(equation: Equation) {
            realm.executeTransaction {
                it.copyToRealm(equation)
            }
        }

        fun getAll(): RealmResults<Equation>? {
            return realm.where(Equation::class.java).findAllAsync()
        }

        fun get(id: ObjectId): Equation? {
            return realm.where(Equation::class.java).equalTo("_id", id)
                .findFirstAsync()
        }

        fun onClear() {
            realm.close()
        }
    }
}
