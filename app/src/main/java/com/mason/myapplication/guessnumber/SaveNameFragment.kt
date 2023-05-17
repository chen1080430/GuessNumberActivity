package com.mason.myapplication.guessnumber

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.mason.myapplication.R
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
    val guessViewModel: GuessViewModel by activityViewModels<GuessViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSavenameBinding.inflate(inflater, container, false)
        _binding!!.apply {
            viewModel = guessViewModel
            lifecycleOwner = requireActivity()
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSave.setOnClickListener {
            var coroutineScope = CoroutineScope(Dispatchers.IO)
            val counter = guessViewModel.counter.value ?: -2

            coroutineScope.launch {
//                AsyncTask.execute {
                val name = binding.editTextPersonName.text.toString()
                // run by async task
                val record = Record(name, counter)
                Log.d(
                    TAG, "onViewCreated: name: $name, counter: $counter"
                )
                val db = Room.databaseBuilder(
                    requireContext(),
                    GameDatabases::class.java, "games.db"
                ).build()
                db.recordDao().insert(record)
//                }
            }
        }

        binding.buttonShowAll.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "SaveNameFragment"
    }
}