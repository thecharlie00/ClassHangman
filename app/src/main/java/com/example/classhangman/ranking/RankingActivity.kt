package com.example.classhangman.ranking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.classhangman.databinding.ActivityGameBinding
import com.example.classhangman.databinding.ActivityRankingBinding

class RankingActivity : AppCompatActivity() {
    lateinit var binding: ActivityRankingBinding
    lateinit var adapter: RankingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = RankingAdapter()
        binding.rankingList.adapter = adapter

        adapter.updateRankingValues(hashMapOf("Alba" to 110, "Marta" to 40, "Sergi" to 50))
    }
}