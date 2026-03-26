package com.timofeev.words

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.timofeev.words.di.Creator
import com.timofeev.words.domain.model.WordDetails
import com.timofeev.words.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val uiState = MutableStateFlow<UiState>(UiState.Idle)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val state by uiState.collectAsStateWithLifecycle()
                DictionaryScreen(
                    state = state,
                    onSearch = ::searchWord
                )
            }
        }
    }

    private fun searchWord(word: String) {
        if (word.isBlank()) return

        lifecycleScope.launch {
            uiState.value = UiState.Loading
            Creator.provideGetWordMeaningUseCase(applicationContext)
                .getWordMeaning(word.trim())
                .collect { resource ->
                    uiState.value = when (resource) {
                        is Resource.Error -> UiState.Error(resource.message ?: getString(R.string.error_unknown))
                        is Resource.Success -> UiState.Success(resource.data.orEmpty())
                    }
                }
        }
    }
}

private sealed interface UiState {
    data object Idle : UiState
    data object Loading : UiState
    data class Success(val words: List<WordDetails>) : UiState
    data class Error(val message: String) : UiState
}

@Composable
private fun DictionaryScreen(
    state: UiState,
    onSearch: (String) -> Unit
) {
    var query by remember { mutableStateOf("hello") }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = query,
                onValueChange = { query = it },
                label = { Text(text = stringResource(R.string.search_label)) },
                singleLine = true
            )

            Button(onClick = { onSearch(query) }) {
                Text(text = stringResource(R.string.search_button))
            }

            when (state) {
                UiState.Idle -> Text(text = stringResource(R.string.search_hint))
                UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error
                )
                is UiState.Success -> ResultList(state.words)
            }
        }
    }
}

@Composable
private fun ResultList(words: List<WordDetails>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(words) { item ->
            Text(
                text = item.word.orEmpty(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            item.meanings.orEmpty().take(2).forEach { meaning ->
                Text(text = "• ${meaning.partOfSpeech.orEmpty()}")
                meaning.definitions.orEmpty().take(2).forEach { definition ->
                    Text(text = definition?.definition.orEmpty())
                }
            }
        }
    }
}
