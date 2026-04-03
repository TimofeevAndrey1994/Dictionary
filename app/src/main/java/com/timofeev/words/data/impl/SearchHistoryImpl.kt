package com.timofeev.words.data.impl

import android.content.Context
import androidx.core.content.edit
import com.timofeev.words.domain.api.SearchHistory
import com.timofeev.words.utils.swap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchHistoryImpl(private val context: Context): SearchHistory {

    private val prefs by lazy {
        context.getSharedPreferences("DCT_PREFS", Context.MODE_PRIVATE)
    }

    override fun getSearchHistory(): Flow<List<String>> = flow {
        val words = prefs.getString("SEARCH_HISTORY","")
        val wordsList = if (words.isNullOrEmpty()) emptyList() else words.split(',').reversed()

        emit(wordsList)
    }

    override suspend fun clearSearchHistory() {
        prefs.edit {
            putString("SEARCH_HISTORY", "")
        }
    }

    override suspend fun addToSearchHistory(value: String) {
        var words = prefs.getString("SEARCH_HISTORY", "") ?: ""
        var wordsList = words.split(',')
        val index = wordsList.indexOf(value)
        if (index > -1) {
            words = wordsList.toMutableList()
                .swap(index, wordsList.lastIndex)
                .joinToString(",")
        }
        else {
            words += (if (words.isEmpty()) value else ",$value")
        }
        wordsList = words.split(",")
        if (wordsList.count() > 10) {
            words = wordsList
                .drop(1)
                .joinToString(",")
        }
        prefs.edit {
            putString("SEARCH_HISTORY", words)
        }
    }
}