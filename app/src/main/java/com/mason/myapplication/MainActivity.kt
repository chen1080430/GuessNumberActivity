package com.mason.myapplication

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mason.myapplication.databinding.ActivityMainBinding
import kotlin.math.absoluteValue


class MainActivity : AppCompatActivity() {

    private val TAG:String="MainActivity"
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel : GuessViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_main) // not work for binding

        val btnGuess = binding.buttonGuess
        viewModel = ViewModelProvider(this).get(GuessViewModel::class.java)
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
                                setClass(this@MainActivity, RecordActivity::class.java)
                                putExtra("counter", viewModel.counter.value)
                                startActivity(this)
                            }
                        })
                        .show()
                }
            }
        }

        btnGuess.setOnClickListener {
            Log.d(TAG, "onClick")
            if (binding.editTextNumber.text != null) {
                binding.editTextNumber.text.toString().toIntOrNull()?.let { it1 ->
                    viewModel.guess(it1)
                }
            }

            }
        // btnGuess long click
        btnGuess.setOnLongClickListener {
            Log.d(TAG, "onLongClick btnGuess and reset")
            viewModel.reset()
            true
        }

    }
}