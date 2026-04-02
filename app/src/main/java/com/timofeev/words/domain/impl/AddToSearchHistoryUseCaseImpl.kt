package com.timofeev.words.domain.impl

import com.timofeev.words.domain.api.AddToSearchHistoryUseCase
import com.timofeev.words.domain.api.SearchHistory

class AddToSearchHistoryUseCaseImpl(private val searchHistory: SearchHistory) :
    AddToSearchHistoryUseCase {
    override suspend fun addToSearchHistory(value: String) {
        searchHistory.addToSearchHistory(value)
    }
}