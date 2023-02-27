package com.example.classhangman.game

import android.os.CountDownTimer
import android.widget.TextView
import android.widget.Toast
import com.example.classhangman.ranking.RankingViewModel
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HangmanModelView(val countdownTextView: TextView) {



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

    fun getNewWord(hagmanTextOuput: TextView) {
        val request = outside.create(ApiHangman::class.java)
        request.getNewWord("es").enqueue(object : Callback<HangmanModel> {
            override fun onResponse(call: Call<HangmanModel>, response: Response<HangmanModel>) {
                hangman = response.body() ?: return
                hagmanTextOuput.text = hangman?.word?.replace("_", "_ ") ?: ""
                countdown.start()
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

                    // is game won?
                    if (hangman?.word?.contains("_") == false) {
                        winGame()
                    }


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