package com.timofeev.words.data.impl

import android.content.Context
import com.timofeev.words.domain.api.SearchHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import androidx.core.content.edit

class SearchHistoryImpl(private val context: Context): SearchHistory {

    private val prefs by lazy {
        context.getSharedPreferences("DCT_PREFS", Context.MODE_PRIVATE)
    }

    override fun getSearchHistory(): Flow<List<String>> = flow {
        val words = prefs.getString("SEARCH_HISTORY","")
        val wordsList = if (words.isNullOrEmpty()) emptyList() else words.split(',')
        emit(wordsList)
    }

    override suspend fun clearSearchHistory() {
        prefs.edit {
            putString("SEARCH_HISTORY", "")
        }
    }

    override suspend fun addToSearchHistory(value: String) {
        var words = prefs.getString("SEARCH_HISTORY", "") ?: ""
        words.split(',').forEach {
            if (it == value) return
        }
        words += (if (words.isEmpty()) value else ",$value")
        prefs.edit {
            putString("SEARCH_HISTORY", words)
        }
    }
}