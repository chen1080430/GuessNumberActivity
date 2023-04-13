package com.mason.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
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

//            auth.signOut()
            //build a alertdialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Sign out?")
            builder.setNegativeButton("Signout") { dialog, which ->
                // Perform login
//            if (loginViewModel.login()) {
//                Log.w(Companion.TAG, "onCreate: login success" )
//                startActivity(Intent(this, MainActivity::class.java))
//            }
                if (auth.currentUser != null) {
                    Log.d(TAG, "onCreate: auth: $auth , currentUser: ${auth.currentUser}")
                    auth.signOut()
                }
            }
            builder.show()

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
        }
    }
}