package com.example.classhangman.game

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.example.classhangman.databinding.ActivityGameBinding
import com.example.classhangman.menu.Login
import com.example.classhangman.menu.MainMenu
import com.example.classhangman.menu.Register
import com.example.classhangman.ranking.RankingActivity
import com.google.firebase.FirebaseApp

class GameActivity : AppCompatActivity() {

    lateinit var binding: ActivityGameBinding

    private val hangmanModelView by viewModels<HangmanModelView>()
    lateinit var animator: GameAnimationsBinder
    lateinit var mediaGame: GameSoundBinder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animator = GameAnimationsBinder(binding).startAnimations()
        mediaGame = GameSoundBinder(binding)

        mediaGame.initMedia(this)

        hangmanModelView.hangman.observe(this) { it ->
            binding.hagmanTextOuput.text = it.word.replace("_", "_ ")

            if (it?.correct == false)
                animator.failAnimation()
                mediaGame.errorSound()

            it?.solution?.let { solution ->
                Toast.makeText(this, solution, Toast.LENGTH_SHORT).show()
            }
        }

        hangmanModelView.alphabet.observe(this) { alphabet ->
            var usedLetters = ""
            alphabet.forEach { (char, isUsed) ->
                if (isUsed)
                    usedLetters += "$char, "
            }
            binding.alphabet.text = usedLetters


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

            binding.keyboard.children.forEach { key ->
                if (key is ImageButton) {
                    key.isEnabled = false
                    key.setColorFilter(0x81989898.toInt())
                }
            }

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)

            if (it == HangmanModelView.GameState.LOST) {
                mediaGame.stopBackgroundMusic()
                builder.setTitle("You lose :(")
                    .setMessage("Retry?")
            } else if (it == HangmanModelView.GameState.WON) {
                animator.winGame()
                mediaGame.stopBackgroundMusic()
                builder.setTitle("You are save")
                    .setMessage("Play again?")
            }

            builder.setPositiveButton("Of course!") { _, _ ->
                finish()
                startActivity(Intent(this, GameActivity::class.java))
                mediaGame.clickSound()
            }
            builder.setNegativeButton("No") { _, _ -> }

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
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
            mediaGame.clickSound()
        }

        binding.countdown.setOnLongClickListener {
            hangmanModelView.getSolution()
            true
        }
    }
}