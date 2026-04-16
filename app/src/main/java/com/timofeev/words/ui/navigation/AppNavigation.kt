package com.timofeev.words.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.timofeev.words.R
import com.timofeev.words.ui.details_word.WordDetailsRoute
import com.timofeev.words.ui.search_word.SearchScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(WordList)

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        val title = when (val navKey = backStack.lastOrNull()) {
            is WordDetails -> navKey.word
            else -> stringResource(R.string.dictionary_title)
        }
        TopAppBar(
            title = { Text(title) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            navigationIcon = {
                if (backStack.lastOrNull() !is WordList) {
                    IconButton(onClick = {
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null
                        )
                    }

                }

            }
        )
    }) { innerPadding ->
        NavDisplay(backStack = backStack, onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        }, entryProvider = entryProvider {
            entry<WordList> {
                SearchScreenRoute(
                    Modifier.padding(innerPadding),
                    onGoToDetails = { word -> backStack.add(WordDetails(word)) })
            }
            entry<WordDetails> { route ->
                WordDetailsRoute(
                    route.word, Modifier.padding(innerPadding), onBackClick = {
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    })
            }
        })
    }
}