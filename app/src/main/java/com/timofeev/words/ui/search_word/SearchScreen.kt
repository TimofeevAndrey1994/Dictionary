package com.timofeev.words.ui.search_word

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.timofeev.words.domain.model.WordDetails
import com.timofeev.words.presentation.search.SeachWordViewModel
import com.timofeev.words.presentation.search.SeachWordViewModelFactory
import com.timofeev.words.presentation.search.SearchResultState

@Composable
fun SearchScreenRoute(modifier: Modifier = Modifier) {
    val context = LocalContext.current.applicationContext
    val seachWordViewModel: SeachWordViewModel = viewModel(
        factory = SeachWordViewModelFactory(
            context
        )
    )
    val uiState = seachWordViewModel.uiState.collectAsState()

    SearchScreen(
        modifier = modifier,
        uiState.value.searchText,
        onValueChange = seachWordViewModel::onSearchTextChange,
        onSearchTextClear = seachWordViewModel::onSearchTextClear,
        searchUiState = uiState.value.resultState,
        showHistory = uiState.value.isHistoryVisible,
        onEditTextChanged = seachWordViewModel::onEditTextFocusChanged,
        searchHistoryWords = uiState.value.searchHistoryList,
        onHistoryItemClick = seachWordViewModel::onHistoryItemClick,
        onHistoryClear = seachWordViewModel::onClearHistory,
        onRepeatQuery = seachWordViewModel::repeatLastQuery
    )
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    textFieldValue: String,
    onValueChange: (String) -> Unit,
    onSearchTextClear: () -> Unit,
    searchUiState: SearchResultState,
    showHistory: Boolean,
    onEditTextChanged: (Boolean) -> Unit,
    searchHistoryWords: List<String>,
    onHistoryItemClick: (String) -> Unit,
    onHistoryClear: () -> Unit,
    onRepeatQuery: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState -> onEditTextChanged(focusState.isFocused) },
            shape = RoundedCornerShape(12.dp),
            maxLines = 1,
            placeholder = { Text("") },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (textFieldValue.isNotEmpty()) Icon(
                    Icons.Default.Clear,
                    contentDescription = null,
                    modifier = Modifier.clickable(onClick = onSearchTextClear)
                )
            },
            value = textFieldValue,
            onValueChange = onValueChange
        )

        if (showHistory) SearchHistory(searchHistoryWords, onHistoryItemClick, onHistoryClear)

       when(searchUiState){
           is SearchResultState.Empty -> {
               Empty(searchUiState.emptyMessage)
           }
           is SearchResultState.Error -> {
               Error(searchUiState.errorMessage, onRepeatQuery = onRepeatQuery)
           }
           SearchResultState.Init -> {}
           SearchResultState.Loading -> {
               Loading()
           }
           is SearchResultState.Success -> {
               WordDetails(searchUiState.data)
           }
       }
    }
}


@Composable
fun SearchHistory(
    items: List<String>,
    onHistoryItemClick: (String) -> Unit,
    onHistoryClear: () -> Unit
) {
    Spacer(Modifier.height(12.dp))
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text("Недавние запросы", modifier = Modifier.align(Alignment.Center))
            Text(
                "Очистить",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable(onClick = { onHistoryClear() }),
            )
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onHistoryItemClick(it) },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Text(text = it)
                    }

                }
            }
        }
    }
}

@Composable
fun Empty(emptyMessage: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(emptyMessage, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun Error(errorMessage: String, onRepeatQuery: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(errorMessage, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Button(onClick = onRepeatQuery) {
                Text("Повторить")
            }
        }
    }
}

@Composable
fun Loading(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(Modifier.height(8.dp))
            Text("Идет загрузка...терпение...")
        }

    }
}

@Composable
fun WordDetails(data: List<WordDetails>) {
    Spacer(Modifier.height(16.dp))
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(data) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {}),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    Text(item.word ?: "")
                }
            }
        }
    }
}
