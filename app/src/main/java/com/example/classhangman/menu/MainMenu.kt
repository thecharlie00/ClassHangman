package com.example.classhangman.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.classhangman.R
import com.example.classhangman.databinding.ActivityLoginBinding
import com.example.classhangman.databinding.ActivityMainMenuBinding
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.AdRequest




class MainMenu : AppCompatActivity() {

    lateinit var binding: ActivityMainMenuBinding
    private lateinit var adView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {}
        adView = binding.adView
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
    }
    override fun onDestroy() {
        adView.destroy()
        super.onDestroy()
    }
}