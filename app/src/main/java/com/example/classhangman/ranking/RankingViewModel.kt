package com.example.classhangman.ranking

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.HashMap

class RankingViewModel : ViewModel() {

    companion object {
        const val RANKING_COLLECTION = "Ranking"
        const val PUNCTUATION_FIELD = "punctuation"
    }


    val ranking = MutableLiveData<HashMap<String, Int>>()
    val shownRanking = MutableLiveData<HashMap<String, Int>>()

    fun loadRanking() {


        val result = HashMap<String, Int>()
        result["Marta"] = 10
        result["Júlia"] = 20
        result["Aniol"] = 30
        result["David"] = 40
        result["Núria"] = 100



        shownRanking.value = HashMap()
        shownRanking.value?.putAll(result)

        // Invoquem observadors
        shownRanking.postValue(shownRanking.value)
        ranking.postValue(result)
    }

    fun preformSearch(text: String) {
        val filteredResults = ranking.value?.filter {
            return@filter it.key.lowercase().contains(text.lowercase())
        } ?: return

        shownRanking.postValue(filteredResults as HashMap<String, Int>)
    }
}