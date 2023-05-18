package com.mason.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mason.myapplication.databinding.FragmentLaunchBinding

import com.mason.myapplication.guessnumber.RecordActivity

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LaunchFragment : Fragment() , FirebaseAuth.AuthStateListener {

    private lateinit var auth: FirebaseAuth

    private val profileViewModel: ProfileViewModel by activityViewModels()

    private var _binding: FragmentLaunchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        _binding = FragmentFirst2Binding.inflate(inflater, container, false)
        _binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_launch, container, false)
        return binding.root

    }

    companion object {
        private const val TAG = "LaunchFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        Log.i(TAG, "XXXXX> onViewCreated: profileViewModel = $profileViewModel")
        binding.buttonFirst.setOnClickListener {
            startActivity(Intent(requireContext(), RecordActivity::class.java))
        }
        profileViewModel.usericon.observe(requireActivity()) {
            Log.i(TAG, "XXXXX , profileViewModel update: usericon = $it")
            binding.textviewTest.text = it.toString()
        }
        profileViewModel.nickname.observe(requireActivity()) {
            Log.i(TAG, "XXXXX , profileViewModel update: nickname = $it")
        }
        binding.buttonProfile.setOnClickListener {
            startActivity(
                Intent(requireContext(), ProfileActivity::class.java)
                    .putExtra("nickname", "")
            )
        }

        binding.buttonAllMessage.setOnClickListener {
            findNavController().navigate(R.id.action_First2Fragment_to_AboutOfflineFragment)

        }
        binding.buttonAdmob.setOnClickListener {
            findNavController().navigate(R.id.action_First2Fragment_to_AdMobFragment)
        }
        binding.buttonYoubike.setOnClickListener {
            findNavController().navigate(R.id.action_First2Fragment_to_ItemFragment)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        auth.currentUser?.uid?.let {

            binding.buttonFirst.visibility = View.VISIBLE
            binding.buttonProfile.visibility = View.VISIBLE
            var firebaseUser = FirebaseDatabase.getInstance().getReference("user")
                .child(it)
            firebaseUser
                .child("icon")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
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
            binding.buttonFirst.visibility = View.INVISIBLE
            binding.buttonProfile.visibility = View.INVISIBLE
        }
    }

}