package com.timofeev.words.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoute: NavKey
@Serializable
data object WordList: AppRoute
@Serializable
data class WordDetails(val word: String): AppRoute