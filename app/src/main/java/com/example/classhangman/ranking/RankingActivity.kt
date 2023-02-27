package com.example.classhangman.ranking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.classhangman.databinding.ActivityGameBinding
import com.example.classhangman.databinding.ActivityRankingBinding

class RankingActivity : AppCompatActivity() {
    lateinit var binding: ActivityRankingBinding
    lateinit var adapter: RankingAdapter

    private val rankingViewModel: RankingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = RankingAdapter()
        binding.rankingList.adapter = adapter

        rankingViewModel.ranking.observe(this) {
            adapter.updateRankingValues(it)
        }

        rankingViewModel.loadRanking()
    }
}