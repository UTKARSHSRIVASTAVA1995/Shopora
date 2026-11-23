package com.example.shopora.ui.category

/**
 * @author Utkarsh Srivastava
 */

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopora.ui.components.ErrorView
import com.example.shopora.ui.components.LoadingView
import com.example.shopora.ui.components.ProductCard
import com.example.shopora.ui.components.SectionHeader

@Composable
fun CategoryScreen(
    onProductClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CategoryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is CategoryUiState.Loading -> LoadingView(message = "Loading category products...")
        is CategoryUiState.Error -> ErrorView(message = state.message, onRetry = { viewModel.loadInitialData() })
        is CategoryUiState.Success -> {
            Column(modifier = modifier.fillMaxSize()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(state.categories) { category ->
                        FilterChip(
                            selected = category == state.selectedCategory,
                            onClick = { viewModel.selectCategory(category) },
                            label = {
                                Text(
                                    text = category.replaceFirstChar { it.uppercase() },
                                    fontWeight = if (category == state.selectedCategory) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item(span = { GridItemSpan(2) }) {
                        SectionHeader(
                            title = "Top Rated in ${state.selectedCategory.replaceFirstChar { it.uppercase() }}",
                            subtitle = "Highest rated products in this category"
                        )
                    }

                    items(state.categoryProducts.topRated, key = { "top_${it.id}" }) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = onProductClick,
                            isTopRatedBadge = true
                        )
                    }

                    item(span = { GridItemSpan(2) }) {
                        SectionHeader(
                            title = "All Products in ${state.selectedCategory.replaceFirstChar { it.uppercase() }}",
                            subtitle = "Browse all items"
                        )
                    }

                    items(state.categoryProducts.allProducts, key = { "all_${it.id}" }) { product ->
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
