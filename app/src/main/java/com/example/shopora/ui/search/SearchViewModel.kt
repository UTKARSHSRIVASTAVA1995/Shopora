package com.example.shopora.ui.search

/**
 * @author Utkarsh Srivastava
 */

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopora.data.repository.ProductRepository
import com.example.shopora.data.repository.ProductRepositoryImpl
import com.example.shopora.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface SearchUiState {
    object Initial : SearchUiState
    object Loading : SearchUiState
    data class Success(val query: String, val results: List<Product>) : SearchUiState
    data class Error(val message: String) : SearchUiState
}

class SearchViewModel(
    private val repository: ProductRepository = ProductRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Initial)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }

    fun performSearch() {
        val currentQuery = _query.value.trim()
        if (currentQuery.isEmpty()) return

        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            repository.searchProducts(currentQuery)
                .onSuccess { results ->
                    _uiState.value = SearchUiState.Success(currentQuery, results)
                }
                .onFailure { error ->
                    _uiState.value = SearchUiState.Error(error.message ?: "Failed to perform search")
                }
        }
    }
}
