package com.example.nush_hack21

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.nush_hack21.model.SerpapiResponse
import com.example.nush_hack21.model.User
import com.example.nush_hack21.ui.image.GetJson
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import com.google.gson.JsonParser
import java.net.URLEncoder

const val RC_SIGN_IN = 1
lateinit var user: User
val auth = FirebaseAuth.getInstance()
const val dbUrl = "http://172.105.114.129:80/"

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().setRequireName(false).build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {
            // Successfully signed in
            if (FirebaseAuth.getInstance().currentUser != null) {
                val uid = auth.currentUser!!.uid

                // Get user data from mongodb
                val query = URLEncoder.encode(uid, "utf-8")
                Log.d("user", dbUrl + "get_user?data=$query")
                GetJson(dbUrl + "get_user?data=$query", object: GetJson.AsyncResponse {
                    override fun processFinish(response: String) {
                        Log.d("User json", response)
//                        user = Gson().fromJson(response, User::class.java)
//
//                        val intent = Intent(applicationContext,NavigationActivity::class.java)
//                        startActivity(intent)
//                        finish()
                    }
                })
            }
        }
    }
}

