package com.timofeev.words.data.dto


import com.google.gson.annotations.SerializedName

data class Phonetic(
    @SerializedName("audio")
    val audio: String? = "",
    @SerializedName("license")
    val license: License? = License(),
    @SerializedName("sourceUrl")
    val sourceUrl: String? = "",
    @SerializedName("text")
    val text: String? = ""
)