package com.mason.myapplication.guessnumber

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.mason.myapplication.R
import com.mason.myapplication.databinding.ActivityMainBinding


class GuessNumberActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "GuessNumberActivity"
    }

//    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: ActivityMainBinding
    val viewModel : GuessViewModel by viewModels<GuessViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater).apply {
//            guessViewModel = viewModel
//            lifecycleOwner = this@GuessNumberActivity
//        }
        binding = DataBindingUtil.setContentView<ActivityMainBinding?>(this, R.layout.activity_main).apply {
            lifecycleOwner = this@GuessNumberActivity
            guessViewModel = viewModel
        }

//        setContentView(binding.root)
//        setContentView(R.layout.activity_main) // not work for binding

        viewModel.counter.observe(this, Observer {
            binding.counter.setText(it.toString())
            Log.d(TAG, "it: $it")
        })

        viewModel.gameResult.observe(this){
            when (it) {
                GuessViewModel.GameResult.BIGGER -> {
                    Toast.makeText(this, "Bigger", Toast.LENGTH_SHORT).show()
                }
                GuessViewModel.GameResult.SMALLER -> {
                    Toast.makeText(this, "Smaller", Toast.LENGTH_SHORT).show()
                }
                GuessViewModel.GameResult.CORRECT -> {
                    Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show()
                    AlertDialog.Builder(this)
                        .setTitle("Game Result")
                        .setMessage("You got the answer in ${viewModel.counter.value} times.")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                            binding.editTextNumber.setText("")
                        })
                        .setNeutralButton("Save", DialogInterface.OnClickListener { dialogInterface, i ->
                            Intent().apply {
                                setClass(this@GuessNumberActivity, RecordActivity::class.java)
                                putExtra("counter", viewModel.counter.value)
                                startActivity(this)
                            }
//                            findNavController().navigate()
                        })
                        .show()
                }
            }
        }
        val btnGuess = binding.buttonGuess
        btnGuess.setOnClickListener {
            if (binding.editTextNumber.text != null) {
                binding.editTextNumber.text.toString().toIntOrNull()?.let { it1 ->
                    viewModel.guess(it1)
                }
            }

            }
        // replaced by databinding
//        btnGuess.setOnLongClickListener {
//            Log.d(TAG, "onLongClick btnGuess and reset")
//            viewModel.reset()
//            true
//        }
    }
}