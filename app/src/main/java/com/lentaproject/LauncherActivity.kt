package com.lentaproject

import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class LauncherActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
       /* if(FirebaseAuth.getInstance().currentUser!=null) {
            startActivity(Intent(applicationContext,MainActivity::class.java))
            finish()
        } else {
            val signInLauncher = registerForActivityResult(
                FirebaseAuthUIActivityResultContract(),
            ) { res ->
                this.onSignInResult(res)
            }
            val providers = arrayListOf(
                AuthUI.IdpConfig.PhoneBuilder().build(),
            )
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
            signInLauncher.launch(signInIntent)
        }*/
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
    }
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            startActivity(Intent(applicationContext,MainActivity::class.java))
        } else {

        }
    }
}