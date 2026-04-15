package com.timofeev.words.ui.details_word

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WordDetailsRoute(word: String, modifier: Modifier = Modifier, onBackClick: () -> Unit) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(word)
    }
}