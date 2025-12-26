package com.timofeev.words.data.dto


import com.google.gson.annotations.SerializedName

data class Definition(
    @SerializedName("antonyms")
    val antonyms: List<String>? = null,
    @SerializedName("definition")
    val definition: String? = null,
    @SerializedName("example")
    val example: String? = null,
    @SerializedName("synonyms")
    val synonyms: List<String>? = null
)