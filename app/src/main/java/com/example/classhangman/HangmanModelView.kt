package com.example.classhangman

import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HangmanModelView {

    var hangman: HangmanModel? = null
    val outside = Retrofit.Builder()
        .baseUrl("http://hangman.enti.cat:5002")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getNewWord(hagmanTextOuput: TextView) {
        val request = outside.create(ApiHangman::class.java)
        request.getNewWord().enqueue(object : Callback<HangmanModel> {
            override fun onResponse(call: Call<HangmanModel>, response: Response<HangmanModel>) {
                hangman = response.body() ?: return
                hagmanTextOuput.text = hangman?.word ?: ""
            }

            override fun onFailure(call: Call<HangmanModel>, t: Throwable) {
                hagmanTextOuput.text = "Error: $t"
            }
        })
    }

    fun guessLetter(letter: Char) {

    }

    fun isGameWon(): Boolean {
        return false
    }
}