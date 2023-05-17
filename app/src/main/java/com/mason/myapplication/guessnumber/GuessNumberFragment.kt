package com.mason.myapplication.guessnumber

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mason.myapplication.GuessViewModel
import com.mason.myapplication.R
import com.mason.myapplication.databinding.FragmentGuessNumberBinding

class GuessNumberFragment : Fragment() {

    private lateinit var _binding: FragmentGuessNumberBinding
    private val binding get() = _binding
    val viewModel: GuessViewModel by activityViewModels<GuessViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGuessNumberBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            guessViewModel = viewModel
            lifecycleOwner = requireActivity()
        }

        binding.buttonGuess.setOnClickListener {
                if (binding.editTextNumber.text != null) {
                    binding.editTextNumber.text.toString().toIntOrNull()?.let { it1 ->
                        viewModel.guess(it1)
                    }
                }

//            binding.editTextNumber.text?.apply {
//                toString().toIntOrNull()?.let { it1 ->
//                    viewModel.guess(it1)
//                }
//            }
        }

        viewModel.gameResult.observe(requireActivity()){
            when (it) {
                GuessViewModel.GameResult.BIGGER -> {
                    Toast.makeText(requireActivity(), "Bigger", Toast.LENGTH_SHORT).show()
                }
                GuessViewModel.GameResult.SMALLER -> {
                    Toast.makeText(requireActivity(), "Smaller", Toast.LENGTH_SHORT).show()
                }
                GuessViewModel.GameResult.CORRECT -> {
                    Toast.makeText(requireActivity(), "Correct", Toast.LENGTH_SHORT).show()
                    AlertDialog.Builder(requireActivity())
                        .setTitle("Game Result")
                        .setMessage("You got the answer in ${viewModel.counter.value} times.")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                            binding.editTextNumber.setText("")
                        })
                        .setNeutralButton("Save", DialogInterface.OnClickListener { dialogInterface, i ->
//                            Intent().apply {
//                                setClass(this@, RecordActivity::class.java)
//                                putExtra("counter", viewModel.counter.value)
//                                startActivity(this)
//                            }
                            findNavController().navigate(R.id.save_name)
                        })
                        .show()
                }
            }
        }
    }

}