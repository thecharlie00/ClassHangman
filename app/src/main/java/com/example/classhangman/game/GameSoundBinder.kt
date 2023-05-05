package com.example.classhangman.game
import android.content.Context
import android.media.MediaPlayer
import com.example.classhangman.R
import com.example.classhangman.databinding.ActivityGameBinding

class GameSoundBinder (binding: ActivityGameBinding){
        lateinit var backgroundMusic: MediaPlayer
        lateinit var brokenJar: MediaPlayer
        lateinit var click: MediaPlayer

        fun initMedia(context: Context){
            backgroundMusic = MediaPlayer.create(context, R.raw.background)
            brokenJar = MediaPlayer.create(context, R.raw.brokenglass)
            click = MediaPlayer.create(context, R.raw.click)

            playBackgroundMusic()
        }

        private  fun playBackgroundMusic(){
            backgroundMusic.start()
        }

        fun clickSound(){
            click.start()
        }

        fun errorSound(){
            brokenJar.start()
        }

        fun stopBackgroundMusic(){
            backgroundMusic.stop()
        }
}