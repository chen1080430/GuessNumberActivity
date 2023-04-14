package com.mason.myapplication

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.FrameLayout.LayoutParams
import android.widget.ImageView
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
//            Snackbar.make(
//                view, (if (isLogin(auth)) {
//                    "You are already signin, do you want to sign out?"
//                } else {
//                    "Sign out"
//                }), Snackbar.LENGTH_LONG
//            )
//                .setAction("Action", null).show()
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
            builder.setPositiveButton("Icon", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    //create dialog that can input number
                    startActivity(Intent(this@LaunchActivity, ProfileActivity::class.java))
                    return

                    val builder = AlertDialog.Builder(this@LaunchActivity)
                    builder.setTitle("Input icon number")
                    builder.setMessage("Please input icon number")
                    val editText = EditText(this@LaunchActivity)
                    builder.setView(editText)

                    var inflate = layoutInflater.inflate(R.layout.icon_select_layout, null, false)
                    inflate.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
//                    builder.setView(inflate)

                    var imageView = ImageView(this@LaunchActivity, null)
//                    LayoutParams.MATCH_PARENT
                    // create imageview which height and weight is match parent and drawable is @drawable/icon_avatar_bear
                    imageView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
//                    imageView.setImageResource(R.drawable.icon_avatar_bear)
//                    builder.setView(imageView)


                    builder.setPositiveButton("OK", object : DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            Log.d(TAG, "onPositiveButton: onClick , ${editText.text}")
                            val icon = editText.text.toString().toIntOrNull() ?: 0
                            FirebaseDatabase.getInstance().getReference("user")
                                .child(auth.currentUser!!.uid)
                                .child("icon")
                                .setValue(icon)
                                .addOnCompleteListener{
                                    Log.d(TAG, "onAuthStateChanged() set icon value complete. icon: $icon")
                                }
                        }
                    })
                    builder.show()
                }
            })
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
        val avatar = arrayOf(
            R.drawable.icon_avatar,
            R.drawable.icon_avatar_bear,
            R.drawable.icon_avatar_cat,
            R.drawable.icon_avatar_cow,
            R.drawable.icon_avatar_dog,
            R.drawable.icon_avatar_lion,
            R.drawable.icon_avatar_monkey,
            R.drawable.icon_avatar_panda,
            R.drawable.icon_avatar_pig,
            R.drawable.icon_avatar_rabbit,
            R.drawable.icon_avatar_tiger,
        )
    }


    override fun onAuthStateChanged(auth: FirebaseAuth) {
        Log.i(Companion.TAG, "XXXXX> onAuthStateChanged: auth = ${FirebaseAuth.getInstance()} , displayName = ${auth.currentUser?.displayName}")

        auth.currentUser?.uid?.let {
            FirebaseDatabase.getInstance().getReference("user")
                .child(it)
                .child("icon")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        Log.d(TAG, "onAuthStateChanged() get icon value complete. icon: ${snapshot.value}")
                        snapshot.value?.let {
                            binding.fab.setImageDrawable(
                                resources.getDrawable(
                                    avatar[when (snapshot.value.toString().toInt()) {
                                        in 0..avatar.size - 1 -> {
                                            snapshot.value.toString().toInt()
                                        }
                                        else -> 0
                                    }], null
                                )
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(TAG, "onAuthStateChanged() get icon value error. error: ${error.message}")
                    }
                }
    //            .addListenerForSingleValueEvent(object : ValueEventListener {
    //                override fun onDataChange(snapshot: DataSnapshot) {
    //                    Log.d(TAG, "onAuthStateChanged() get icon value complete. icon: ${snapshot.value}")
    //                    binding.fab.setImageDrawable(resources.getDrawable(
    //                        avatar[snapshot.value.toString().toInt()], null))
    //                }
    //
    //                override fun onCancelled(error: DatabaseError) {
    //                    Log.d(TAG, "onAuthStateChanged() get icon value error. error: ${error.message}")
    //                }
    //            }
                )
        }

        auth.currentUser?.also {
            Log.w(Companion.TAG, "onAuthStateChanged: login success , currentUser= ${auth.currentUser} , " +
                    "uid: ${auth.currentUser?.uid}" )
        } ?: run {
//            Log.w(Companion.TAG, "onAuthStateChanged: login success , currentUser= ${auth.currentUser} , " +
//                    "uid: ${auth.currentUser?.uid}" )
            Log.w(TAG, "onAuthStateChanged: please sign up" )
//            signup()
        }
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