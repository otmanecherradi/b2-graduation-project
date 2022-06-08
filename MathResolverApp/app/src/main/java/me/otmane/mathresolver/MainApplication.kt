package me.otmane.mathresolver

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        val realmName: String = "My Project"
        val config = RealmConfiguration.Builder().name(realmName).build()

        val backgroundThreadRealm : Realm = Realm.getInstance(config)

    }
}