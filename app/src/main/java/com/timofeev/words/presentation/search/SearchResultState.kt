package com.timofeev.words.presentation.search

import com.timofeev.words.domain.model.WordDetails

sealed class SearchResultState {
    object Init: SearchResultState()
    object Loading: SearchResultState()
    class Error(errorMessage: String): SearchResultState()
    class Empty(emptyMessage: String): SearchResultState()
    class Success(data: List<WordDetails>): SearchResultState()
}