package com.example.classhangman.ranking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.classhangman.databinding.ItemRankingBinding

class RankingAdapter : RecyclerView.Adapter<RankingAdapter.RankingItemViewHolder>() {

    var sortedRanking = ArrayList<Pair<String, Int>>()

    fun updateRankingValues(rank: HashMap<String, Int>) {
        // returns a list of sorted usernames and punctuations
        sortedRanking = rank.keys.sortedByDescending  {
            rank[it]
        }.map { it to (rank[it] ?: 0) } as ArrayList
        notifyDataSetChanged()
    }

    inner class RankingItemViewHolder(binding: ItemRankingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val username = binding.username
        val punctuation = binding.puntuation
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRankingBinding.inflate(layoutInflater, parent, false)
        return RankingItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankingItemViewHolder, position: Int) {
        holder.username.text = sortedRanking[position].first
        holder.punctuation.text = sortedRanking[position].second.toString()
    }

    override fun getItemCount(): Int {
        return sortedRanking.size
    }
}