package com.example.classhangman

data class HangmanModel(
    val token: String = "",
    val language: String = "",
    val maxTries: Int = 0,
    val word: String = "",
    val incorrectGuesses: Int = 0
)
