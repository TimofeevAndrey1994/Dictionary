package com.timofeev.words.presentation.search

data class SearchUiState(
    val searchText: String = "",
    val isHistoryVisible: Boolean = false,
    val searchHistoryList: List<String> = emptyList(),
    val resultState: SearchResultState = SearchResultState.Init
)
