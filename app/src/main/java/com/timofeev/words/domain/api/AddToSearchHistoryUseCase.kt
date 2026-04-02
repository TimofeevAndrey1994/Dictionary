package com.timofeev.words.domain.api

interface AddToSearchHistoryUseCase {
    suspend fun addToSearchHistory(value: String)
}