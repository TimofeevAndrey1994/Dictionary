package com.timofeev.words.data.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.timofeev.words.domain.api.SearchHistory
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SearchHistoryImpl(private val context: Context) : SearchHistory {

    private val prefs by lazy {
        context.getSharedPreferences("DCT_PREFS", Context.MODE_PRIVATE)
    }

    override fun getSearchHistory(): Flow<List<String>> = callbackFlow {

        fun readHistory(): List<String> {
            val words = prefs.getString("SEARCH_HISTORY", "")
            return if (words.isNullOrEmpty()) emptyList() else words.split(',').reversed()
        }



        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == "SEARCH_HISTORY") {
                val updated = readHistory()
                trySend(updated)
            }
        }

        prefs.registerOnSharedPreferenceChangeListener(listener)
        trySend(readHistory())
        awaitClose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    override suspend fun clearSearchHistory() {
        prefs.edit {
            putString("SEARCH_HISTORY", "")
        }
    }

    override suspend fun addToSearchHistory(value: String) {
        val updatedWords = (prefs.getString("SEARCH_HISTORY", "") ?: "")
            .split(",")
            .filter { it.isNotBlank() }
            .toMutableList()
            .apply {
                remove(value)
                add(value)
            }
            .takeLast(10)
            .joinToString(",")

        prefs.edit {
            putString("SEARCH_HISTORY", updatedWords)
        }
    }
}