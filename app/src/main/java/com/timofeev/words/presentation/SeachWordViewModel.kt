package com.timofeev.words.presentation

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

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    private var previousSearchedText = ""

    fun onSearchTextChange(value: String) {
        _searchText.value = value
        searchWithDebaunce()
    }

    fun onSearchTextClear(){
        _searchText.value = ""
    }

    private fun searchWithDebaunce() {
        val searchText = _searchText.value.trim()
        if (previousSearchedText == searchText) return

        job?.cancel()
        job = viewModelScope.launch {
            delay(2000L)
            Creator.provideGetWordMeaningUseCase(context).getWordMeaning(searchText)
                .collect { res ->
                    when(res){
                        is Resource.Error<*> -> {
                            println(res.message)
                        }
                        is Resource.Success<*> -> {
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