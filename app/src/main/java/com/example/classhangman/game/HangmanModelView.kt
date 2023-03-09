package com.example.classhangman.game

import android.os.CountDownTimer
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HangmanModelView : ViewModel() {

    var hangman = MutableLiveData<HangmanModel>()

    val alphabet = MutableLiveData<HashMap<Char, Boolean>>().also { alphaCreation ->
        hashMapOf<Char, Boolean>().also {
            ('a'..'z').forEach { c ->
                it[c] = false
            }
            alphaCreation.postValue(it)
        }
    }

    private val outside = Retrofit.Builder()
        .baseUrl("http://hangman.enti.cat:5002")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val alphabet = hashMapOf<Char, Boolean>().also {
        ('a'..'z').forEach { c ->
            it[c] = false
        }
    }

    var remainingTime = 60

    val countdown = object : CountDownTimer(60 * 1000, 250) {
        override fun onTick(millisUntilFinished: Long) {
            countdownTextView.text = "${millisUntilFinished / 1000} s"
            remainingTime = (millisUntilFinished / 1000).toInt()
        }

        override fun onFinish() {
            countdownTextView.text = "Game over!"
        }
    }

    fun getNewWord() {
        val request = outside.create(ApiHangman::class.java)
        request.getNewWord("es").enqueue(object : Callback<HangmanModel> {
            override fun onResponse(call: Call<HangmanModel>, response: Response<HangmanModel>) {
                hangman.postValue(response.body() ?: return)
            }

            override fun onFailure(call: Call<HangmanModel>, t: Throwable) {
            }
        })
    }

    fun guessLetter(letter: Char): Boolean {
        val request = outside.create(ApiHangman::class.java)
        val formattedLetter = letter.lowercase()[0]

        // Verify if letter is already checked
        if (alphabet.value?.get(formattedLetter) == true)
            return false

        request.guessLetter(
            mapOf(
                "token" to hangman.value?.token,
                "letter" to formattedLetter.toString()
            )
        ).enqueue(object : Callback<HangmanModel> {

            override fun onResponse(call: Call<HangmanModel>, response: Response<HangmanModel>) {
                hangman.postValue(response.body() ?: return)

                alphabet.value?.apply {
                    set(formattedLetter, true)
                    alphabet.postValue(this)
                }
            }

            override fun onFailure(call: Call<HangmanModel>, t: Throwable) {
            }
        })

        return true
    }
    private fun winGame() {
        countdown.cancel()
        countdownTextView.text = "Congratulations!!"

        val firebase = FirebaseFirestore.getInstance()
        val collection = firebase.collection(RankingViewModel.RANKING_COLLECTION)
        collection.document(username).update(RankingViewModel.PUNCTUATION_FIELD, getPunctuation())
    }

    fun getPunctuation(): Int {
        return (hangman?.word?.length ?: 0) * 10 - (hangman?.incorrectGuesses ?: 0) * 5 + remainingTime
    }
}