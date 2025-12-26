package com.timofeev.words.data.dto


import com.google.gson.annotations.SerializedName

data class Meaning(
    @SerializedName("antonyms")
    val antonyms: List<String?>? = null,
    @SerializedName("definitions")
    val definitions: List<Definition?>? = null,
    @SerializedName("partOfSpeech")
    val partOfSpeech: String? = null,
    @SerializedName("synonyms")
    val synonyms: List<String?>? = null
)