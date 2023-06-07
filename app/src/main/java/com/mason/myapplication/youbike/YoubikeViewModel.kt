package com.mason.myapplication.youbike

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class YoubikeViewModel : ViewModel() {

    val filterListSize = MutableLiveData<Int>(0)

    private val _timer = MutableLiveData<Int>(0)
    val timer: LiveData<Int> = _timer
    var timerCountung : CountDownTimer? = null

    fun updateTimer() {
        if (timerCountung != null) {
            timerCountung?.cancel()
        }
        timerCountung = object : CountDownTimer(15000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timer.value = ((millisUntilFinished / 1000).toInt())
            }

            override fun onFinish() {
                _timer.value = 0
            }
        }.start()
    }

}
