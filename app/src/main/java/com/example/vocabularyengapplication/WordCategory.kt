package com.example.vocabularyengapplication

import androidx.annotation.ColorRes

enum class WordCategory (val title: String, @ColorRes color: Int){
    ALL_CATEGORIES("All Category", R.color.black),
    ADJECTIVE("Adjective", R.color.green_51DC2E),
    PREPOSITION("Preposition", R.color.red_DC572E),
    VERB("Verb", R.color.yellow_D6B709),
    NOUN("Noun", R.color.purple_9A96FF)
}

enum class ListWordState{
    NORMAL,
    REMOVE
}