package com.mason.myapplication

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.FrameLayout.LayoutParams
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mason.myapplication.databinding.ActivityLaunchBinding
import java.util.*

class LaunchActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    private lateinit var mAdView: AdView
    private val REQUEST_CODE_SMS_PERMISSION: Int = 101
    private val RC_SIGN_IN: Int = 100
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityLaunchBinding
    private lateinit var auth: FirebaseAuth

    var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var navView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_content_launch)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.First2Fragment, R.id.AboutOfflineFragment, R.id.YoubikeFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        MobileAds.initialize(this) {}

        auth = FirebaseAuth.getInstance()

        binding.fab.setOnClickListener { view ->

            if (mInterstitialAd != null) {
                mInterstitialAd?.show(this)
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.")
            }
            //build a alertdialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle(auth.currentUser?.run {"Hi ${auth.currentUser?.displayName ?: "Guest"}"} ?: run { "Login in"  })
            builder.setMessage(auth.currentUser?.run {"Email:${auth.currentUser?.email} \nDo you want to sign out?}"}?:run {"Please login to continue"})
            builder.setNegativeButton(auth.currentUser?.run { "Signout"  }?:run { "Login"}) { dialog, which ->
                auth.currentUser?.run {
                    auth.signOut()
                } ?: run {
                    signup()
                }
                Log.d(TAG, "XXXXX > onNegativeButton: onClick , ${auth.currentUser?.run { "signOut."}?: run{"signup / login."} }")
            }
            auth.currentUser?.run {
                builder.setPositiveButton("Icon", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        //create dialog that can input number
                        startActivity(Intent(this@LaunchActivity, ProfileActivity::class.java))
                        return
                    }
                })
            }
            builder.show()
            Log.w(TAG, "XXXXX > onCreate: auth: $auth , currentUser : ${auth.currentUser}" )

        }

        //get RECEIVE_SMS permission
        if (grantSmsPermission().also {
            Log.d(TAG, "XXXXX > onCreate: grantSmsPermission: $it" )
            } ) {
            //start smsReceiver

        }

        mAdView = binding.adView
        var adRequest = AdRequest.Builder().build()
//        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object: AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }

/*
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                adError?.toString()?.let { Log.d(TAG, it) }
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "XXXXX> Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })

        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }

         */


    }

    private fun grantSmsPermission() : Boolean {
        // check sms permission
        val smsPermission = android.Manifest.permission.RECEIVE_SMS
        val grant = checkCallingOrSelfPermission(smsPermission)
        if (grant != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            // request permission
            requestPermissions(arrayOf(smsPermission), REQUEST_CODE_SMS_PERMISSION)
        }
        return grant == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_SMS_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "XXXXX> onRequestPermissionsResult: permission granted.")
                } else {
                    Log.d(TAG, "XXXXX> onRequestPermissionsResult: permission denied.")
                }
            }
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
                        Log.d(TAG, "XXXXX > onAuthStateChanged() get icon value complete. icon: ${snapshot.value}")
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
                )
        }

        auth.currentUser?.also {
            Log.w(Companion.TAG, "onAuthStateChanged: login success , currentUser= ${auth.currentUser} , " +
                    "uid: ${auth.currentUser?.uid}" )
        } ?: run {
            Log.w(TAG, "onAuthStateChanged: please sign up" )
//            signup()
        }
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