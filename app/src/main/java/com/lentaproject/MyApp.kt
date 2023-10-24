package com.lentaproject

import android.app.Application
import android.util.Log
import com.firedata.StoreObject
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

class MyApp: Application() {

    override fun onCreate() {
        Firebase.initialize(context = this)
        Firebase.appCheck.addAppCheckListener {
            Log.d("TAG",it.token)
        }
        Firebase.appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance(),
        )
        Firebase.appCheck.setTokenAutoRefreshEnabled(true)
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseAuth.getInstance().firebaseAuthSettings.forceRecaptchaFlowForTesting(true)
        FirebaseAuth.getInstance().firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
    }


    companion object {
        var list = mutableListOf<StoreObject>()
    }
}