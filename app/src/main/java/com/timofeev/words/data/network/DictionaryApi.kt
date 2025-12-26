package com.timofeev.words.data.network

import com.timofeev.words.data.dto.WordDetailsResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {
    @GET("/api/v2/entries/en/{word}")
    fun getWordMeaning(@Path("word") hello: String): Flow<WordDetailsResponse>
}