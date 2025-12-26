package com.timofeev.words.data.dto


import com.google.gson.annotations.SerializedName

data class WordDetailsResponseItem(
    @SerializedName("license")
    val license: License? = License(),
    @SerializedName("meanings")
    val meanings: List<Meaning>? = listOf(),
    @SerializedName("phonetics")
    val phonetics: List<Phonetic>? = listOf(),
    @SerializedName("sourceUrls")
    val sourceUrls: List<String>? = listOf(),
    @SerializedName("word")
    val word: String? = ""
)