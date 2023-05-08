package com.example.classhangman.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.notification.NotificationListenerService.Ranking
import com.example.classhangman.R
import com.example.classhangman.databinding.ActivityLoginBinding
import com.example.classhangman.databinding.ActivityMainMenuBinding
import com.example.classhangman.game.GameActivity

import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.AdRequest




class MainMenu : AppCompatActivity() {

    lateinit var binding: ActivityMainMenuBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)


        binding.playBtn2.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }
        binding.rankingBtn.setOnClickListener {
            startActivity(Intent(this, Ranking::class.java))
        }
        binding.quitBtn.setOnClickListener {
            finishAffinity()
            System.exit(0)
        }
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        setContentView(binding.root)

    }
    override fun onDestroy() {
        binding.adView.destroy()
        super.onDestroy()
    }
}