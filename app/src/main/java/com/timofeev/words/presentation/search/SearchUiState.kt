package com.timofeev.words.presentation.search

data class SearchUiState(
    val searchText: String = "",
    val isHistoryVisible: Boolean = false,
    val resultState: SearchResultState = SearchResultState.Init
)
