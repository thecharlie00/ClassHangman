package com.example.classhangman.game

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.classhangman.databinding.ActivityGameBinding
import com.example.classhangman.ranking.RankingActivity

class GameActivity : AppCompatActivity() {

    lateinit var binding: ActivityGameBinding
    val hangmanModelView by viewModels<HangmanModelView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)


        hangmanModelView.hangman.observe(this) {
            binding.hagmanTextOuput.text = it.word.replace("_", "_ ")

            if (it?.correct == false)
                Toast.makeText(this, "This letter is not in the word!", Toast.LENGTH_SHORT).show()
        }

        hangmanModelView.alphabet.observe(this) {
            var usedLetters = ""
            it.forEach { (char, isUsed) ->
                if (isUsed)
                    usedLetters += "$char, "
            }
            binding.alphabet.text = usedLetters
        }

        hangmanModelView.getNewWord()


        binding.guessButton.setOnClickListener {
            val letter = binding.guessLetterInput.text.getOrNull(0)

            if (letter != null) {
                if (!hangmanModelView.guessLetter(letter))
                    Toast.makeText(this, "You already used letter $letter", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, "You must submit a letter", Toast.LENGTH_SHORT).show()

            binding.guessLetterInput.setText("")
        }

        binding.gotoRanking.setOnClickListener {
            val intent = Intent(this, RankingActivity::class.java)
            startActivity(intent)
        }
    }
}