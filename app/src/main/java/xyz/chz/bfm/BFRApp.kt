package xyz.chz.bfm

import android.app.Application
import android.content.Context
import android.os.StrictMode
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BFRApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
    }
}