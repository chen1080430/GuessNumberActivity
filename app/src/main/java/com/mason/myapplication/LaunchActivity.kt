package com.mason.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.mason.myapplication.databinding.ActivityLaunchBinding

class LaunchActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    private val RC_SIGN_IN: Int = 100
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityLaunchBinding
    lateinit var loginViewModel : LoginViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_launch)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        var loginViewModel = LoginViewModel()
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        auth = FirebaseAuth.getInstance()



        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "You are already signin, do you want to sign out?", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
//            if (loginViewModel.login()) {
//                Log.w(Companion.TAG, "onCreate: login success" )
//                startActivity(Intent(this, MainActivity::class.java))
//            }

            // print auth data
            Log.d(TAG, "onCreate: auth: $auth , currentUser: ${auth.currentUser}" +
                    ", displayname: ${auth.currentUser?.displayName}" +
                    ", email: ${auth.currentUser?.email}")

//            auth.signOut()
            //build a alertdialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle(auth.currentUser?.run {"Hi ${auth.currentUser?.displayName ?: "Guest"}"} ?: run { "Login in"  })
            builder.setMessage(auth.currentUser?.run {"Email:${auth.currentUser?.email} \nDo you want to sign out?}"}?:run {"Please login to continue"})
            builder.setNegativeButton(auth.currentUser?.run { "Signout"  }?:run { "Login"}) { dialog, which ->
                // Perform login
//            if (loginViewModel.login()) {
//                Log.w(Companion.TAG, "onCreate: login success" )
//                startActivity(Intent(this, MainActivity::class.java))
//            }
//                if (auth.currentUser != null) {
//                    Log.d(TAG, "onCreate: auth: $auth , currentUser: ${auth.currentUser}")
//                    auth.signOut()
//                }
                auth.currentUser?.run {
//                    Log.d(TAG, "onCreate: auth: $auth , currentUser: ${auth.currentUser}")
                    auth.signOut()
                } ?: run {
                    signup()
                }
                Log.d(TAG, "onNegativeButton: onClick , ${auth.currentUser?.run { "signOut."}?: run{"signup / login."} }")
            }
            builder.show()
            Log.w(TAG, "onCreate: auth: $auth , currentUser : ${auth.currentUser}" )

        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_launch)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    companion object {
        private const val TAG = "LaunchActivity"
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {

//        Log.w(TAG, "onAuthStateChanged: auth: $auth , currentUser: ${auth.currentUser}" )
        binding.fab.setImageDrawable(resources.getDrawable(
            (if (isLogin(auth)) {
                R.drawable.icon_avatar_lion
            } else {
                R.drawable.icon_avatar
            }), null))
        auth.currentUser?.also {
            Log.w(Companion.TAG, "onAuthStateChanged: login success , currentUser= ${auth.currentUser} , " +
                    "uid: ${auth.currentUser?.uid}" )
           /*
            FirebaseDatabase.getInstance().getReference("test_ref")
                .child(it.uid)
                .child("test_child")
                .setValue("test_value")
                .addOnCompleteListener{
                    Log.d(TAG, "onAuthStateChanged() set test value complete.")
                }
            */
        } ?: run {
//            Log.w(Companion.TAG, "onAuthStateChanged: login success , currentUser= ${auth.currentUser} , " +
//                    "uid: ${auth.currentUser?.uid}" )
            Log.w(TAG, "onAuthStateChanged: please sign up" )
//            signup()
        }
       /*
        if (auth.currentUser == null) {
            Log.w(Companion.TAG, "onAuthStateChanged: login failed" )
            startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                    .setAvailableProviders(
                        listOf(
                            AuthUI.IdpConfig.GoogleBuilder().build(),
                            AuthUI.IdpConfig.EmailBuilder().build()
                        )
                    )
                    .setIsSmartLockEnabled(false)
                    .build()
                , RC_SIGN_IN)
        } else {
            Log.w(Companion.TAG, "onAuthStateChanged: login success , currentUser= ${auth.currentUser} , " +
                    "uid: ${auth.currentUser?.uid}" )
        }*/
    }

    private fun isLogin(auth: FirebaseAuth) :Boolean{
        return auth.currentUser != null
    }

    private fun signup() {
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(
                    listOf(
                        AuthUI.IdpConfig.GoogleBuilder().build(),
                        AuthUI.IdpConfig.EmailBuilder().build()
                    )
                )
                .setIsSmartLockEnabled(false)
                .build(), RC_SIGN_IN
        )
    }
}