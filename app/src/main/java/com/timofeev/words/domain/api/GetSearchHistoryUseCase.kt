package com.timofeev.words.domain.api

import kotlinx.coroutines.flow.Flow

interface GetSearchHistoryUseCase {
    suspend fun getSearchHistory(): Flow<List<String>>
}