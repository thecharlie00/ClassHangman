package com.example.classhangman.game

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
}