package com.example.classhangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.classhangman.databinding.ActivityGameBinding

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
                hangmanModelView.guessLetter(char)
            else
                Toast.makeText(this, "You must submit a letter", Toast.LENGTH_SHORT).show()
        }
    }
}