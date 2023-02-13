package com.example.classhangman.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.classhangman.databinding.ActivityGameBinding
import com.example.classhangman.ranking.RankingActivity

class GameActivity : AppCompatActivity() {

    lateinit var binding: ActivityGameBinding
    lateinit var hangmanModelView: HangmanModelView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hangmanModelView = HangmanModelView()
        hangmanModelView.getNewWord(binding.hagmanTextOuput)


        binding.guessButton.setOnClickListener {
            val char = binding.guessLetterInput.text.getOrNull(0)
            if (char != null)
                hangmanModelView.guessLetter(char, binding.hagmanTextOuput, binding.alphabet, this)
            else
                Toast.makeText(this, "You must submit a letter", Toast.LENGTH_SHORT).show()

            binding.guessLetterInput.setText("")
        }

        binding.gotoRanking.setOnClickListener {
            val intent = Intent(this, RankingActivity::class.java)
            startActivity(intent)
        }
    }
}