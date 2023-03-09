package com.example.classhangman.game

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.example.classhangman.databinding.ActivityGameBinding
import com.example.classhangman.ranking.RankingActivity

class GameActivity : AppCompatActivity() {

    lateinit var binding: ActivityGameBinding

    val hangmanModelView by viewModels<HangmanModelView>()
    lateinit var animator: GameAnimationsBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animator = GameAnimationsBinder(binding).startAnimations()

        hangmanModelView.hangman.observe(this) {
            binding.hagmanTextOuput.text = it.word.replace("_", "_ ")

            if (it?.correct == false) {
                animator.failAnimation()
//                animator.loseGame()
            }
        }

        hangmanModelView.alphabet.observe(this) { alphabet ->
            var usedLetters = ""
            alphabet.forEach { (char, isUsed) ->
                if (isUsed)
                    usedLetters += "$char, "
            }
            binding.alphabet.text = usedLetters

            // Lock keyboard
            binding.keyboard.children.forEach { key ->
                val letter = resources.getResourceName(key.id).lowercase().last()
                if (alphabet[letter] == true && key is ImageButton) {
                    key.isEnabled = false
                    key.setColorFilter(0x81989898.toInt())
                }
            }
        }

        hangmanModelView.getNewWord()

        binding.keyboard.children.forEach { key ->
            key.setOnClickListener {
                val letter = resources.getResourceName(it.id).last()

                if (!hangmanModelView.guessLetter(letter))
                    Toast.makeText(this, "You already used letter $letter", Toast.LENGTH_SHORT)
                        .show()
            }
        }

        binding.gotoRanking.setOnClickListener {
            val intent = Intent(this, RankingActivity::class.java)
            startActivity(intent)
        }
    }
}