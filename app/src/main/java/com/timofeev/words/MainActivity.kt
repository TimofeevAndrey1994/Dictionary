package com.timofeev.words

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.timofeev.words.di.Creator
import com.timofeev.words.utils.Resource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    DictionaryScreen(
                        loadWord = { query, onResult ->
                            Creator.provideGetWordMeaningUseCase(applicationContext)
                                .getWordMeaning(query)
                                .collect { onResult(it) }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DictionaryScreen(
    loadWord: suspend (String, (Resource<*>) -> Unit) -> Unit
) {
    var stateText by remember { mutableStateOf("Загрузка...") }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        loadWord("hello") { resource ->
            when (resource) {
                is Resource.Success -> {
                    isLoading = false
                    stateText = resource.data.toString()
                }

                is Resource.Error -> {
                    isLoading = false
                    stateText = resource.message ?: "Произошла ошибка"
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        }

        Text(
            text = stateText,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DictionaryScreenPreview() {
    MaterialTheme {
        DictionaryScreen { _, onResult ->
            onResult(Resource.Success("Preview result"))
        }
    }
}
