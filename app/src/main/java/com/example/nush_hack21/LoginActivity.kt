package com.example.nush_hack21

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.nush_hack21.model.User
import com.example.nush_hack21.model.UserResponse
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson


const val RC_SIGN_IN = 1
lateinit var user: User
val auth = FirebaseAuth.getInstance()
const val dbUrl = "http://172.105.114.129/"

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
                val queue = Volley.newRequestQueue(applicationContext)
                val getUrl = dbUrl + "get_user?data=$uid"
                val stringRequest = StringRequest(
                    Request.Method.GET, getUrl,
                    { response ->
                        Log.i("GetUser", response)

                        val userResponse = Gson().fromJson(response, UserResponse::class.java)
                        if (userResponse.userExists) user = userResponse.user!!
                        else {
                            // Create user object and convert to json
                            user = User(uid, displayName = auth.currentUser!!.displayName)
                            val addUrl = dbUrl + "add_user?data=${Gson().toJson(user)}"

                            // Add user to mongodb
                            val addRequest = StringRequest(
                                Request.Method.GET, addUrl, { addResponse ->
                                    Log.i("AddUser", addResponse)
                                }, {})
                            queue.add(addRequest)
                        }

                        startNavigation()

                        Log.i("user", user.toString())
                    }, {})

                queue.add(stringRequest)
            }
        }
    }

    private fun startNavigation() {
        val intent = Intent(applicationContext, NavigationActivity::class.java)
        startActivity(intent)
        finish()
    }
}

