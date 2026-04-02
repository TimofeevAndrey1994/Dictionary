package com.timofeev.words.domain.impl

import com.timofeev.words.domain.api.GetSearchHistoryUseCase
import com.timofeev.words.domain.api.SearchHistory
import kotlinx.coroutines.flow.Flow

class GetSearchHistoryUseCaseImpl(private val searchHistory: SearchHistory) : GetSearchHistoryUseCase {
    override suspend fun getSearchHistory(): Flow<List<String>> {
        return searchHistory.getSearchHistory()
    }
}