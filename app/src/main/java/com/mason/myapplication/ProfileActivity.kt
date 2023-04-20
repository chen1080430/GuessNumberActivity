package com.mason.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.mason.myapplication.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity(), OnClickListener{
    private var selectedIcon: Int = 0
    private lateinit var imageViewList: List<ImageView>
//    private lateinit var profileViewModel: ProfileViewModel
   val profileViewModel by viewModels<ProfileViewModel>()

    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        Log.i(TAG, "XXXXX> onViewCreated: profileViewModel = $profileViewModel")

        imageViewList = listOf<ImageView>(
            binding.imageView1,
            binding.imageView2,
            binding.imageView3,
            binding.imageView4,
            binding.imageView5,
            binding.imageView6,
            binding.imageView7,
            binding.imageView8,
            binding.imageView9,
            binding.imageView10
        )
        imageViewList.forEach() {
            it.setOnClickListener(this)
        }
        binding.updateButton.setOnClickListener(this)
        profileViewModel.usericon.observe(this){
            Log.i(TAG, "XXXXX , profileViewModel update: usericon = $it")
        }
        profileViewModel.nickname.observe(this) {
            Log.i(TAG, "XXXXX , profileViewModel update: nickname = $it")
        }
        profileViewModel.getUserProfile()
        profileViewModel.usericon.observe(this) {
            selectedIcon = it
            imageViewList.forEachIndexed { index, imageView ->
                imageView.setBackgroundResource(if (selectedIcon == index + 1) R.drawable.select_background else 0)
            }
        }
        profileViewModel.nickname.observe(this) {
            binding.editTextTextPersonName.setText(it)
        }
    }

    override fun onClick(view: View?) {
        imageViewList.forEachIndexed { index, imageView ->
            imageView.takeIf { it == view }?.let {
                selectedIcon = index+1
                run{ Log.d(TAG,"XXXXX imageview index: $selectedIcon") }
            }
        }
        imageViewList.forEachIndexed { index, imageView ->
            imageView.setBackgroundResource(if(selectedIcon==index+1) R.drawable.select_background else 0)
        }
        when(view) {
            binding.updateButton -> {
                Log.i(TAG, "XXXXX , onClick: update button clicked")
                val nickname = binding.editTextTextPersonName.text.toString()
                nickname.let {
                    if (it.isEmpty()) {
                        profileViewModel.updateIcon(selectedIcon)
                    } else {
                        profileViewModel.updateProfile(nickname, selectedIcon)
                   }
                }
                // update nickname in firebase
//                profileViewModel.updateNickname(nickname)
//                profileViewModel.updateIcon(selectedIcon)
//                profileViewModel.updateProfile(nickname, selectedIcon)
            }
        }
        Log.i(TAG, "XXXXX , onClick: displayname = ${FirebaseAuth.getInstance().currentUser?.displayName} , selectedIcon = $selectedIcon")
    }

    companion object {
        private const val TAG = "ProfileActivity"
    }
}