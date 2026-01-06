package com.timofeev.words.domain.model

data class Meaning(
    val antonyms: List<String?>? = null,
    val definitions: List<Definition?>? = null,
    val partOfSpeech: String? = null,
    val synonyms: List<String?>? = null
)
