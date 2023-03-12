package com.example.classhangman.game

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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

            if (it?.correct == false)
                animator.failAnimation()
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

        hangmanModelView.gameSate.observe(this) {
            if (it == HangmanModelView.GameState.PLAYING)
                return@observe

            // Lock all keys
            binding.keyboard.children.forEach { key ->
                if (key is ImageButton) {
                    key.isEnabled = false
                    key.setColorFilter(0x81989898.toInt())
                }
            }

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)

            if (it == HangmanModelView.GameState.LOST) {
                animator.loseGame()
                builder.setTitle("You lose :(")
                    .setMessage("Retry?")
            } else if (it == HangmanModelView.GameState.WON) {
                animator.winGame()
                builder.setTitle("You are save")
                    .setMessage("Play again?")
            }

            builder.setPositiveButton("Yes!") { _, _ ->
                finish()
                startActivity(Intent(this, GameActivity::class.java))
            }

            builder.create().show()
        }

        hangmanModelView.remainingTime.observe(this) {
            binding.countdown.text = "$it  s"
        }

        hangmanModelView.getNewWord()

        binding.keyboard.children.forEach { key ->
            key.setOnClickListener {
                val letter = resources.getResourceName(it.id).last()
                hangmanModelView.guessLetter(letter)
            }
        }

        binding.gotoRanking.setOnClickListener {
            val intent = Intent(this, RankingActivity::class.java)
            startActivity(intent)
        }
    }
}