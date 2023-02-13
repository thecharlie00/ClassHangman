package com.example.classhangman.game

import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HangmanModelView {

    val username = "gg class"
    var hangman: HangmanModel? = null
    val outside = Retrofit.Builder()
        .baseUrl("http://hangman.enti.cat:5002")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val alphabet = hashMapOf<Char, Boolean>().also {
        ('a'..'z').forEach { c ->
            it[c] = false
        }
    }

    fun getNewWord(hagmanTextOuput: TextView) {
        val request = outside.create(ApiHangman::class.java)
        request.getNewWord("es").enqueue(object : Callback<HangmanModel> {
            override fun onResponse(call: Call<HangmanModel>, response: Response<HangmanModel>) {
                hangman = response.body() ?: return
                hagmanTextOuput.text = hangman?.word?.replace("_", "_ ") ?: ""
            }

            override fun onFailure(call: Call<HangmanModel>, t: Throwable) {
                hagmanTextOuput.text = "Error: $t"
            }
        })
    }

    fun guessLetter(
        letter: Char,
        hagmanTextOuput: TextView,
        alphabetTextView: TextView,
        context: GameActivity
    ) {
        val request = outside.create(ApiHangman::class.java)
        val formattedLetter = letter.lowercase()

        if (alphabet[formattedLetter[0]] == true) {
            Toast.makeText(context, "You already used letter $letter", Toast.LENGTH_SHORT).show()
            return
        }

        request.guessLetter(
            mapOf(
                "token" to hangman?.token,
                "letter" to formattedLetter
            )
        ).enqueue(object : Callback<HangmanModel> {

            override fun onResponse(call: Call<HangmanModel>, response: Response<HangmanModel>) {
                hangman = response.body() ?: return


                if (hangman?.correct == true) {
                    hagmanTextOuput.text = hangman?.word?.replace("_", "_ ") ?: ""
                } else {
                    Toast.makeText(
                        context,
                        "Letter $letter is not in the word!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                alphabet[formattedLetter[0]] = true

                var usedLetters = ""
                alphabet.forEach { (char, isUsed) ->
                    if (isUsed)
                        usedLetters += "$char, "
                }
                alphabetTextView.text = usedLetters
            }

            override fun onFailure(call: Call<HangmanModel>, t: Throwable) {
                hagmanTextOuput.text = "Error: $t"
            }
        })
    }

    fun isGameWon(): Boolean {
        return false
    }

    fun getPunctuation(): Int {
        return (hangman?.word?.length ?: 0) * 10 - (hangman?.incorrectGuesses ?: 0) * 5
    }
}