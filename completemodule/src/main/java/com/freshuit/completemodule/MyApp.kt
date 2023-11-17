package com.completemodule

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

class MyApp : Application() {
    override fun onCreate() {
        Firebase.initialize(context = this)
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}