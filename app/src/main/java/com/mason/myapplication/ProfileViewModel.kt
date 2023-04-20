package com.mason.myapplication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope

class ProfileViewModel : ViewModel(){
    var auth: FirebaseAuth
    var selectedIcon = 0
    var usericon = MutableLiveData<Int>()
    var nickname = MutableLiveData<String>("N/A")

    init {
        auth = FirebaseAuth.getInstance()

       /*
        FirebaseDatabase.getInstance().getReference("user")
            .child(auth.currentUser!!.uid)
            .child("nickname")
            .addValueEventListener(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    nickname.value = snapshot.value.toString()
                    Log.d(TAG, "onDataChange: it:${snapshot.value} " +
                            "nickname: ${nickname.value} , displayName: ${auth.currentUser?.displayName}")
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    Log.d(TAG, "onCancelled: ${error.message}")
                }
            })

        FirebaseDatabase.getInstance().getReference("user")
            .child(auth.currentUser!!.uid)
            .child("icon")
            .addValueEventListener(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    usericon.value = snapshot.value.toString().toInt()
                    Log.d(TAG, "onDataChange: "+
                            "usericon: ${usericon.value} , displayName: ${auth.currentUser?.displayName}")
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    Log.d(TAG, "onCancelled: ${error.message}")
                }

            })
*/

    }
    fun updateIcon(selectIcon : Int) {
        selectedIcon = selectIcon
        auth.currentUser?.uid?.let {
            FirebaseDatabase.getInstance().getReference("user")
                .child(it)
                .child("icon")
                .setValue(selectedIcon)
        }

//        usericon.value = selectedIcon
        Log.d(TAG, "XXXXX , updateIcon: selectedIcon: $selectedIcon, usericon.value: ${usericon.value}")
    }

    fun updateNickname(name:String){
        nickname.value = name.toString()
        auth.currentUser?.uid?.let {
            FirebaseDatabase.getInstance().getReference("user")
                .child(it)
                .child("nickname")
                .setValue(nickname.value)
        }
        Log.i(TAG, "XXXXX> updateNickName: nickname: ${nickname.value}")
    }

    fun updateProfile(name : String, selectionIcon: Int) {

        auth.currentUser?.uid?.let {


            var userProfile = UserProfile().apply {
                nickname=name
                icon=selectionIcon
            }
            val childUpdates = mapOf<String, Any>(
                "$it" to userProfile
            )

            var reference = FirebaseDatabase.getInstance().getReference("user")
            reference.updateChildren(childUpdates).addOnCompleteListener{
                    Log.d(TAG, "XXXXX> updateProfile: update profile complete.")
                    nickname.value = name
                    usericon.value = selectionIcon
                }

        }
    }

    fun testForUpdateIconLiveData(i: Int) {
        Log.i(TAG, "XXXXX> testForUpdateIconLiveData: i: $i")
//        usericon.value = i
        usericon.postValue(i)
    }

    fun getUserProfile() {
        auth.currentUser?.uid?.let {
            FirebaseDatabase.getInstance().getReference("user")
                .child(it)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        Log.d(TAG, "XXXXX> getUserProfile: onDataChange: snapshot: $snapshot")
                        snapshot.child("nickname").value?.let {
                            nickname.value = it.toString()
                        }
                        snapshot.child("icon").value?.let {
                            usericon.value = it.toString().toInt()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(TAG, "XXXXX> getUserProfile: onCancelled: ${error.message}")
                    }
                })

        }
    }


    companion object {
        private const val TAG = "ProfileViewModel"
    }

}
