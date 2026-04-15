package com.timofeev.words.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.timofeev.words.ui.details_word.WordDetailsRoute
import com.timofeev.words.ui.search_word.SearchScreenRoute

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(WordList)

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = entryProvider {
            entry<WordList> {
                SearchScreenRoute(
                    modifier,
                    onGoToDetails = { word -> backStack.add(WordDetails(word)) })
            }
            entry<WordDetails> { route ->
                WordDetailsRoute(
                    route.word,
                    modifier,
                    onBackClick = {
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    })
            }
        })

}