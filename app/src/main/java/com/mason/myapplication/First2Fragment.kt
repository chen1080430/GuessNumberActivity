package com.mason.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.mason.myapplication.databinding.FragmentFirst2Binding
import kotlin.math.log

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class First2Fragment : Fragment() , FirebaseAuth.AuthStateListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var profileViewModel: ProfileViewModel
    //    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentFirst2Binding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirst2Binding.inflate(inflater, container, false)
        return binding.root

    }

    companion object {
        private const val TAG = "First2Fragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
//        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
         profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
/*
        // viewmodel observer for nickname
        profileViewModel.nickname.observe(viewLifecycleOwner) {
            Log.d(Companion.TAG, "nickname observer called : $it")
            binding.textviewWelcome1.setText("Hi! $it")
        }
*/
/*
        // viewmodel observer
        profileViewModel.usericon.observe(viewLifecycleOwner) {icon ->
            Log.d(Companion.TAG, "usericon observer called : $icon")
            binding.imageViewAvatar.setImageResource(LaunchActivity.avatar[icon])
        }
*/
//        binding.textviewWelcome1.setText("Hi! ${profileViewModel.auth.currentUser?.displayName}")
//        binding.imageViewAvatar.setImageResource(getDrawable(this, LaunchActivity.avatar[profileViewModel.selectedIcon]))
//        binding.imageViewAvatar.setImageResource(LaunchActivity.avatar[profileViewModel.selectedIcon])

        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_First2Fragment_to_Second2Fragment)
//            if (loginViewModel.login()) {
            /*
            requireActivity().also {
                    startActivity(Intent(it, MainActivity::class.java))
        }
            */
/*
            FirebaseDatabase.getInstance()
                .getReference("user")
                .child("auth1")
                .child("test_child")
                .setValue("test_value")
                .addOnCompleteListener {
                    Log.d(Companion.TAG, "Firebase Database set test value complete.")
                    //create toast to show success
                    Toast.makeText(requireContext(), "Firebase Database set test value complete.", Toast.LENGTH_SHORT).show()
                }
*/

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        Log.d(Companion.TAG, "onStart() called")

        FirebaseAuth.getInstance().addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        FirebaseApp.initializeApp(requireContext())
        Log.d(Companion.TAG, "onCreate() called with: requireActivity() = ${requireActivity()}")
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        auth.currentUser?.uid?.let {
            var firebaseUser = FirebaseDatabase.getInstance().getReference("user")
                .child(it)
            firebaseUser
                .child("icon")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        Log.d(
                            TAG,
                            "XXXXX> onAuthStateChanged() get icon value complete. icon: ${snapshot.value}"
                        )
                        snapshot.value?.let {
                            binding.imageViewAvatar.setImageDrawable(
                                resources.getDrawable(
                                    LaunchActivity.avatar[when (snapshot.value.toString().toInt()) {
                                        in 0..LaunchActivity.avatar.size - 1 -> {
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
                })
            firebaseUser
                .child("nickname")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        Log.d(TAG, "XXXXX> onAuthStateChanged() get nickname value complete. nickname: ${snapshot.value}")
                        snapshot.value?.let {
                            Log.d(TAG, "XXXXX> onDataChange: snapshot.value: ${snapshot.value}")
                            binding.textviewWelcome1.setText("Hi! ${snapshot.value}")
                        }?: run{
                            Log.d(TAG, "XXXXX> onDataChange: snapshot.value is null, show displayname: ${auth.currentUser?.displayName}")
                            binding.textviewWelcome1.setText("Hi! Your display name:\n${auth.currentUser?.displayName}")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(TAG, "XXXXX> onAuthStateChanged() get nickname value error. error: ${error.message}")
                    }
                })
        }?: run {
            Log.d(TAG, "XXXXX> onAuthStateChanged: no user login, show default icon and nickname.")
            binding.imageViewAvatar.setImageDrawable(resources.getDrawable(LaunchActivity.avatar[0]))
            binding.textviewWelcome1.setText(getString(R.string.welcome_please_login))
        }
    }

}