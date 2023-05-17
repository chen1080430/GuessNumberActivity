package com.mason.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mason.myapplication.guessnumber.GuessViewModel
import io.mockk.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GuessViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var guessViewModel: GuessViewModel

    @Before
    fun setUp() {
        guessViewModel = GuessViewModel()
        guessViewModel.counter.value = 0
        guessViewModel.guessCount = 0

        println(
            "GuessViewModel Unit Test, SetUp: " +
                    "\ncounter: ${guessViewModel.counter.value}" +
                    "\ngameResult: ${guessViewModel.gameResult.value}"
        )
    }

    @After
    fun cleanUp() {
    }

    @Test
    fun reset_CounterAnd_ReturnZero() {
        for (i in 1..3) {
            guessViewModel.guess(1)
        }
        assertFalse(guessViewModel.counter.value != 3)
        guessViewModel.reset()
        assertEquals(0, guessViewModel.counter.value)
    }

    @Test
    fun guessResult_Bigger_returnTrue() {
        var secretNumber = guessViewModel.secretNumber
        guessViewModel.guess(secretNumber - 1)
        assertEquals(GuessViewModel.GameResult.BIGGER, guessViewModel.gameResult.value)
    }

    @Test
    fun guessResult_Smaller_returnTrue() {
        var secretNumber = guessViewModel.secretNumber
        guessViewModel.guess(secretNumber + 1)
        assertEquals(GuessViewModel.GameResult.SMALLER, guessViewModel.gameResult.value)
    }

    @Test
    fun guessResult_Correct_returnTrue() {
        guessViewModel.reset()
        var secretNumber = guessViewModel.secretNumber
        guessViewModel.guess(secretNumber - 1)
        assertTrue(GuessViewModel.GameResult.CORRECT != guessViewModel.gameResult.value)
        guessViewModel.guess(secretNumber)
        assertEquals(GuessViewModel.GameResult.CORRECT, guessViewModel.gameResult.value)
    }

    @Test
    fun guessResult_Correct_returnFalse() {
        var secretNumber = guessViewModel.secretNumber
        guessViewModel.guess(secretNumber - 1)
        assertTrue(GuessViewModel.GameResult.CORRECT != guessViewModel.gameResult.value)
        guessViewModel.guess(secretNumber + 1)
        assertTrue(GuessViewModel.GameResult.CORRECT != guessViewModel.gameResult.value)
    }

    @Test
    fun counter_guess10times_returnTrue() {
        val observer = mockk<Observer<Int>>(relaxed = true)
        guessViewModel.counter.observeForever(observer)
        guessViewModel.reset()
        assertEquals(guessViewModel.counter.value, 0)


        for (i in 1..3) {
            guessViewModel.guess(1)
            verify { observer.onChanged(i) }
        }
        assertEquals(guessViewModel.counter.value, 3)


        for (i in 4..8) {
            guessViewModel.guess(i)
            verify { observer.onChanged(i) }
        }
        assertEquals(guessViewModel.counter.value, 8)
        guessViewModel.counter.removeObserver(observer)
        guessViewModel.reset()
    }

    @Test
    fun testLivedata() {

        val observer = mockk<Observer<Int>>(relaxed = true)
        // if mockk is not relaxed, then we need to mock every function of observer
//        val observer = mockk<Observer<Int>>()
//        every { observer.onChanged(any()) } just Runs

        // create observer with out mockk
//        val observer = Observer<Int> {
//            println("guessViewModel.counter = ${guessViewModel.counter.value}")
//        }

        guessViewModel.counter.observeForever(observer)

        guessViewModel.guess(3)
        var secretNumber = guessViewModel.secretNumber
        assertEquals(
            if (secretNumber < 3) GuessViewModel.GameResult.SMALLER else GuessViewModel.GameResult.BIGGER,
            guessViewModel.gameResult.value
        )
        guessViewModel.guess(secretNumber)
        assertEquals(GuessViewModel.GameResult.CORRECT, guessViewModel.gameResult.value)
//        assertEquals(2, guessViewModel.counter.getOrAwaitValue())
        assertEquals(2, guessViewModel.counter.value)

        verify { observer.onChanged(any()) }

//        verify(observer).onChanged(2)
//        guessViewModel.counter.removeObserver(observer)

    }


}