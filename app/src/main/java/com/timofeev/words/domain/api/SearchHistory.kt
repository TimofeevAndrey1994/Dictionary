package com.timofeev.words.domain.api

import kotlinx.coroutines.flow.Flow

interface SearchHistory {
    fun getSearchHistory(): Flow<List<String>>
    suspend fun clearSearchHistory()
    suspend fun addToSearchHistory(value: String)
}