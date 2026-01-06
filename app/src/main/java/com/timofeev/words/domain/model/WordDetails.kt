package com.timofeev.words.domain.model

data class WordDetails(
    val meanings: List<Meaning>? = null,
    val phonetics: List<Phonetic>? = null,
    val word: String? = ""
)
