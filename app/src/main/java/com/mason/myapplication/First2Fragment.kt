package com.mason.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

import com.mason.myapplication.databinding.FragmentFirst2Binding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class First2Fragment : Fragment() , FirebaseAuth.AuthStateListener {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_First2Fragment_to_Second2Fragment)
//            if (loginViewModel.login()) {
                requireActivity().also {
                    startActivity(Intent(it, MainActivity::class.java))
                }
//            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
//        FirebaseApp.initializeApp(requireActivity())

//        FirebaseAuth.getInstance().addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()

//        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        FirebaseApp.initializeApp(requireContext())
        Log.d(TAG, "onCreate() called with: requireActivity() = ${requireActivity()}")
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        if (auth.currentUser==null) {

        }else{
            Log.d(Companion.TAG, "onAuthStateChanged() called with: auth = \n$auth , "+
                    " currentUser = ${auth.currentUser}" +
                    "\nuid = ${auth.currentUser?.uid}")
        }
    }

    companion object {
        private const val TAG = "First2Fragment"
    }
}