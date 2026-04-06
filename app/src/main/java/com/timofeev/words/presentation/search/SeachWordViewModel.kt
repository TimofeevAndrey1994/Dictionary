package com.timofeev.words.presentation.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.timofeev.words.di.Creator
import com.timofeev.words.domain.api.AddToSearchHistoryUseCase
import com.timofeev.words.domain.api.ClearSearchHistoryUseCase
import com.timofeev.words.domain.api.GetSearchHistoryUseCase
import com.timofeev.words.domain.api.GetWordMeaningUseCase
import com.timofeev.words.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//--- Context в будущем будем получать из DI (но пока что так)
class SeachWordViewModel(
    private val getWordMeaningUseCase: GetWordMeaningUseCase,
    private val addToSearchHistoryUseCase: AddToSearchHistoryUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase
) : ViewModel() {
    private var job: Job? = null
    private var isEditTextFocused: Boolean = false
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadHistory()
        }
    }

    private suspend fun addToHistory(value: String) {
        addToSearchHistoryUseCase.addToSearchHistory(value)
    }

    fun onSearchTextChange(value: String) {
        _uiState.value = _uiState.value.copy(searchText = value.replace(",",""))
        isMustShowHistory()
        searchWithDebaunce()
    }

    fun onHistoryItemClick(value: String) {
        _uiState.value = _uiState.value.copy(searchText = value)
        //--- Если нажали на то же слово - оно тоже должно искаться
        searchWithDebaunce(force = true)
        isMustShowHistory()
    }

    private suspend fun loadHistory(){
        getSearchHistoryUseCase.getSearchHistory().collect { words ->
            _uiState.value = _uiState.value.copy(searchHistoryList = words)
            isMustShowHistory()
        }
    }

    fun onClearHistory() {
        viewModelScope.launch {
            clearSearchHistoryUseCase.clearSearchHistory()
        }
    }

    fun onSearchTextClear(){
        _uiState.value = _uiState.value.copy(searchText = "")
        job?.cancel()
        _uiState.value = _uiState.value.copy(resultState = SearchResultState.Init)
        isMustShowHistory()
    }

    fun onEditTextFocusChanged(isFocused: Boolean) {
        isEditTextFocused = isFocused
        isMustShowHistory()
    }

    fun repeatLastQuery(){
        searchWithDebaunce(force = true)
    }

    private fun isMustShowHistory() {
        val value = _uiState.value
        if (value.searchText.trim().isEmpty() && isEditTextFocused && value.searchHistoryList.isNotEmpty()) {
            _uiState.value = value.copy(isHistoryVisible = true)
        } else {
            _uiState.value = value.copy(isHistoryVisible = false)
        }
    }

    private fun searchWithDebaunce(force: Boolean = false) {
        job?.cancel()
        val searchText = _uiState.value.searchText.trim()
        if (searchText.isEmpty()) {
            onSearchTextClear()
            return
        }

        job = viewModelScope.launch {
            if (!force) delay(2000L)
            _uiState.value = _uiState.value.copy(resultState = SearchResultState.Loading)
            getWordMeaningUseCase.getWordMeaning(searchText)
                .collect { res ->
                    when(res){
                        is Resource.Error<*> -> {
                            _uiState.value =
                                _uiState.value.copy(
                                    resultState = SearchResultState.Error(
                                        res.message ?: ""
                                    )
                                )
                        }
                        is Resource.Success<*> -> {
                            if (res.data.isNullOrEmpty()) {
                                _uiState.value =
                                    _uiState.value.copy(resultState = SearchResultState.Empty("Список пуст!"))  //--- Потом вынесу в ресурсы
                            } else {
                                addToHistory(searchText)
                                _uiState.value =
                                    _uiState.value.copy(resultState = SearchResultState.Success(res.data))
                            }
                            println(res.data)
                        }
                    }
                }
        }
    }
}

class SeachWordViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SeachWordViewModel::class.java)) {
            return SeachWordViewModel(
                Creator.provideGetWordMeaningUseCase(context),
                Creator.provideAddToSearchHistoryUseCase(context),
                Creator.provideClearSearchHistoryUseCase(context),
                Creator.provideGetSearchHistoryUseCase(context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}