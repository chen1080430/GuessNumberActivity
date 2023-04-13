package com.mason.myapplication

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
class LoginViewModel : ViewModel() {

    init {
        lateinit var auth: FirebaseAuth
// ...
// Initialize Firebase Auth
//        auth = Firebase.auth
    }

    fun login(): Boolean {

        return true
    }

    fun logout() {
//        Firebase.auth.signOut()
    }

    // add a function to check if user is logged in
//    fun isLoggedIn(): Boolean {
//        return Firebase.auth.currentUser != null
//    }

    //add auth state listener


}
