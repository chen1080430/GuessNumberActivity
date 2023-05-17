package com.mason.myapplication.guessnumber

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.mason.myapplication.R
import com.mason.myapplication.databinding.ActivityRecordBinding

class RecordActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRecordBinding
    val guessViewModel: GuessViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_record)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Game already reset.", Snackbar.LENGTH_SHORT)
                .show()

            navController.navigate(R.id.go_guess_number_page)
            guessViewModel.reset()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_record)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}