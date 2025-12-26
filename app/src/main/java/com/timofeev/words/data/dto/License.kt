package com.timofeev.words.data.dto


import com.google.gson.annotations.SerializedName

data class License(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("url")
    val url: String? = null
)