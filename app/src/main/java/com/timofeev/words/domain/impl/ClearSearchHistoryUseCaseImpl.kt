package com.timofeev.words.domain.impl

import com.timofeev.words.domain.api.ClearSearchHistoryUseCase
import com.timofeev.words.domain.api.SearchHistory

class ClearSearchHistoryUseCaseImpl(private val searchHistory: SearchHistory) :
    ClearSearchHistoryUseCase {
    override suspend fun clearSearchHistory() {
        searchHistory.clearSearchHistory()
    }
}
