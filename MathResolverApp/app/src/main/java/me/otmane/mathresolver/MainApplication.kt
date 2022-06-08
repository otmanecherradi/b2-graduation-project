package me.otmane.mathresolver

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .name("mathresolver.realmdb")
            .deleteRealmIfMigrationNeeded()
            .allowWritesOnUiThread(true)
            .allowQueriesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(config);
    }
}