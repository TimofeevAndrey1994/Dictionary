package com.timofeev.words.data.network

import com.timofeev.words.data.dto.WordDetailsResponseItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class RetrofitNetworkClient(private val dictionaryApi: DictionaryApi) {
    suspend fun doRequest(word: String) {
        dictionaryApi.getWordMeaning(word)
    }
}