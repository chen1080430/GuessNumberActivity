package com.mason.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.mason.myapplication.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity(), OnClickListener{
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        binding.imageView1.setOnClickListener(this)
        binding.imageView2.setOnClickListener(this)
        binding.imageView3.setOnClickListener(this)
        binding.imageView4.setOnClickListener(this)
        binding.imageView5.setOnClickListener(this)
        binding.imageView6.setOnClickListener(this)
        binding.imageView7.setOnClickListener(this)
        binding.imageView8.setOnClickListener(this)
        binding.imageView9.setOnClickListener(this)
        binding.imageView10.setOnClickListener(this)

        binding.updateButton.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        //set background to imageview which is clicked.
        var selectedIcon = 0

        when(view) {
            binding.imageView1 -> selectedIcon = 1
            binding.imageView2 -> selectedIcon = 2
            binding.imageView3 -> selectedIcon = 3
            binding.imageView4 -> selectedIcon = 4
            binding.imageView5 -> selectedIcon = 5
            binding.imageView6 -> selectedIcon = 6
            binding.imageView7 -> selectedIcon = 7
            binding.imageView8 -> selectedIcon = 8
            binding.imageView9 -> selectedIcon = 9
            binding.imageView10 -> selectedIcon = 10
            binding.updateButton -> {
                Log.i(Companion.TAG, "onClick: update button clicked")
                // get nickname from edittext
                val nickname = binding.editTextTextPersonName.text.toString()
                // update nickname in firebase
                profileViewModel.updateNickname(nickname)

            }
        }
        binding.imageView1.setBackgroundResource(if(selectedIcon==1) R.drawable.select_background else 0)
        binding.imageView2.setBackgroundResource(if(selectedIcon==2) R.drawable.select_background else 0)
        binding.imageView3.setBackgroundResource(if(selectedIcon==3) R.drawable.select_background else 0)
        binding.imageView4.setBackgroundResource(if(selectedIcon==4) R.drawable.select_background else 0)
        binding.imageView5.setBackgroundResource(if(selectedIcon==5) R.drawable.select_background else 0)
        binding.imageView6.setBackgroundResource(if(selectedIcon==6) R.drawable.select_background else 0)
        binding.imageView7.setBackgroundResource(if(selectedIcon==7) R.drawable.select_background else 0)
        binding.imageView8.setBackgroundResource(if(selectedIcon==8) R.drawable.select_background else 0)
        binding.imageView9.setBackgroundResource(if(selectedIcon==9) R.drawable.select_background else 0)
        binding.imageView10.setBackgroundResource(if(selectedIcon==10) R.drawable.select_background else 0)

        profileViewModel.updateIcon(selectedIcon)
        Log.i(Companion.TAG, "onClick: auth = ${FirebaseAuth.getInstance()} , selectedIcon = $selectedIcon")
    }

    companion object {
        private const val TAG = "ProfileActivity"
    }
}