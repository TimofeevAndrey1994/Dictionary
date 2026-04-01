package com.timofeev.words.ui.search_word

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.timofeev.words.presentation.search.SeachWordViewModel
import com.timofeev.words.presentation.search.SeachWordViewModelFactory
import com.timofeev.words.presentation.search.SearchResultState
import com.timofeev.words.presentation.search.SearchUiState

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
        searchUiState = uiState.value.resultState
    )
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    textFieldValue: String,
    onValueChange: (String) -> Unit,
    onSearchTextClear: () -> Unit,
    searchUiState: SearchResultState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
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
       when(searchUiState){
           is SearchResultState.Empty -> {
               Empty()
           }
           is SearchResultState.Error -> {
               Error()
           }
           SearchResultState.Init -> {}
           SearchResultState.Loading -> {
               Loading()
           }
           is SearchResultState.Success -> {
               WordDetails()
           }
       }
    }
}

@Composable
fun Empty(){
  TODO()
}

@Composable
fun Error(){
    TODO()
}

@Composable
fun Loading(){
    TODO()
}

@Composable
fun WordDetails(){
    TODO()
}
