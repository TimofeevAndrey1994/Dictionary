package com.timofeev.words.presentation.search

import com.timofeev.words.domain.model.WordDetails

sealed class SearchResultState {
    object Init: SearchResultState()
    object Loading: SearchResultState()
    class Error(val errorMessage: String): SearchResultState()
    class Empty(val emptyMessage: String): SearchResultState()
    class Success(val data: List<WordDetails>): SearchResultState()
}