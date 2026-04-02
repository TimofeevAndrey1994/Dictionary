package com.timofeev.words.domain.api

interface ClearSearchHistoryUseCase {
    suspend fun clearSearchHistory()
}