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

    val firebase = FirebaseFirestore.getInstance()
    val ranking = MutableLiveData<HashMap<String, Int>>()

    fun loadRanking() {
        val collection = firebase.collection(RANKING_COLLECTION)
        collection.get().addOnCompleteListener { collectionData ->
            val result = HashMap<String, Int>()
            val data = collectionData.result

            data.documents.forEach {
//                result["Marta"] = 10
                result[it.id] = it.get(PUNCTUATION_FIELD).toString().toIntOrNull() ?: 0
            }

            ranking.postValue(result)
        }
    }
}