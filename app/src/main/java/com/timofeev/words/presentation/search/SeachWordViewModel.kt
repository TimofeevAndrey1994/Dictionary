package com.timofeev.words.presentation.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.timofeev.words.di.Creator
import com.timofeev.words.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//--- Context в будущем будем получать из DI (но пока что так)
class SeachWordViewModel(private val context: Context) : ViewModel() {
    private var job: Job? = null

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private var previousSearchedText = ""

    fun onSearchTextChange(value: String) {
        _uiState.value = _uiState.value.copy(searchText = value)
        searchWithDebaunce()
    }

    fun onSearchTextClear(){
        _uiState.value = _uiState.value.copy(searchText = "")
    }

    private fun searchWithDebaunce() {
        val searchText = _uiState.value.searchText
        if (previousSearchedText == searchText) return

        job?.cancel()
        job = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(resultState = SearchResultState.Loading)
            delay(2000L)
            Creator.provideGetWordMeaningUseCase(context).getWordMeaning(searchText)
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
                                _uiState.value =
                                    _uiState.value.copy(resultState = SearchResultState.Success(res.data))
                            }
                            println(res.data)
                        }
                    }
                }
            previousSearchedText = searchText
        }
    }
}

class SeachWordViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SeachWordViewModel::class.java)) {
            return SeachWordViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}