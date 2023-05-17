package com.mason.myapplication.guessnumber

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GuessViewModel : ViewModel() {

    private val TAG: String = "GuessViewModel"
    val counter = MutableLiveData<Int>(0)
    val gameResult = MutableLiveData<GameResult>()

    // create init block
    init {
//        Log.d(TAG, "init: secretNumber: $secretNumber , guessCount: $guessCount")
        Log.d(TAG, "XXXXX> GuessViewModel init.\n counter.value: ${counter.value}")
        reset()
    }

    fun guess(num: Int) {
        var n = counter.value ?: 0
        n = n + 1
        counter.value = n
        // compare num with secretNumber and return result
        when (num - secretNumber) {
            0 -> {
                gameResult.value = GameResult.CORRECT
            }
            in 1..Int.MAX_VALUE -> {
                gameResult.value = GameResult.SMALLER
            }
            else -> {
                gameResult.value = GameResult.BIGGER
            }
        }
    }


    // create secret number between 1 to 10
    var secretNumber: Int = 0
    var guessCount = 0

    //create reset secret number and guess count
    fun reset() {
        secretNumber = (1..10).random()
        guessCount = 0
        counter.value = 0
        Log.d(TAG, "reset: secretNumber: $secretNumber , guessCount: $guessCount")
    }

    // create enum GameResult
    enum class GameResult {
        BIGGER, SMALLER, CORRECT
    }

}