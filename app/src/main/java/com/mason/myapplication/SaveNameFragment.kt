package com.mason.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.mason.myapplication.databinding.FragmentSavenameBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SaveNameFragment : Fragment() {
    private var _binding: FragmentSavenameBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSavenameBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val intent = requireActivity().intent
        var counterFromMain = intent.getIntExtra("counter", -1)
        binding.textviewFirst.text = "Record counter: $counterFromMain"

        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            var coroutineScope = CoroutineScope(Dispatchers.IO)
            coroutineScope.launch {
//                AsyncTask.execute {
                val name = binding.editTextPersonName.text.toString()
                // run by async task
                val record = Record(name, counterFromMain)
                Log.d(
                    Companion.TAG, "onViewCreated: name: $name, counter: $counterFromMain"
                )
                val db = Room.databaseBuilder(
                    requireContext(),
                    GameDatabases::class.java, "games.db"
                ).build()
                db.recordDao().insert(record)
//                }
            }
        }

        binding.buttonFrag2.setOnClickListener({
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "SaveNameFragment"
    }
}