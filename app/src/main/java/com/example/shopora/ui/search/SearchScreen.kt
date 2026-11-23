package com.example.shopora.ui.search

/**
 * @author Utkarsh Srivastava
 */

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopora.ui.components.EmptyView
import com.example.shopora.ui.components.ErrorView
import com.example.shopora.ui.components.LoadingView
import com.example.shopora.ui.components.ProductCard
import com.example.shopora.ui.components.SectionHeader

@Composable
fun SearchScreen(
    onProductClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.query.collectAsState()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.onQueryChanged(it) },
                placeholder = { Text("Search products...") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onQueryChanged("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { viewModel.performSearch() }),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { viewModel.performSearch() }) {
                Text("Search")
            }
        }

        when (val state = uiState) {
            is SearchUiState.Initial -> EmptyView(message = "Type a product name or category and click Search.")
            is SearchUiState.Loading -> LoadingView(message = "Searching catalogue...")
            is SearchUiState.Error -> ErrorView(message = state.message, onRetry = { viewModel.performSearch() })
            is SearchUiState.Success -> {
                if (state.results.isEmpty()) {
                    EmptyView(message = "No products match '${state.query}'.")
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item(span = { GridItemSpan(2) }) {
                            SectionHeader(
                                title = "Search Results",
                                subtitle = "Found ${state.results.size} items matching '${state.query}'"
                            )
                        }

                        items(state.results, key = { it.id }) { product ->
                            ProductCard(
                                product = product,
                                onProductClick = onProductClick
                            )
                        }
                    }
                }
            }
        }
    }
}
