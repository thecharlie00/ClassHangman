package com.example.classhangman

import retrofit2.Call
import retrofit2.http.GET

interface ApiHangman {

    @GET("/new?lang=cat")
    fun getNewWord(): Call<HangmanModel>
}