package com.example.shopora.ui.category

/**
 * @author Utkarsh Srivastava
 */

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopora.data.repository.ProductRepository
import com.example.shopora.data.repository.ProductRepositoryImpl
import com.example.shopora.domain.model.CategoryProducts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CategoryUiState {
    object Loading : CategoryUiState
    data class Success(
        val categories: List<String>,
        val selectedCategory: String,
        val categoryProducts: CategoryProducts
    ) : CategoryUiState
    data class Error(val message: String) : CategoryUiState
}

class CategoryViewModel(
    private val repository: ProductRepository = ProductRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<CategoryUiState>(CategoryUiState.Loading)
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = CategoryUiState.Loading
            repository.getCategories()
                .onSuccess { categories ->
                    if (categories.isNotEmpty()) {
                        selectCategory(categories.first(), categories)
                    } else {
                        _uiState.value = CategoryUiState.Error("No categories found")
                    }
                }
                .onFailure { error ->
                    _uiState.value = CategoryUiState.Error(error.message ?: "Failed to load categories")
                }
        }
    }

    fun selectCategory(category: String, existingCategories: List<String>? = null) {
        viewModelScope.launch {
            val categories = existingCategories ?: (uiState.value as? CategoryUiState.Success)?.categories ?: emptyList()
            _uiState.value = CategoryUiState.Loading
            repository.getProductsByCategory(category)
                .onSuccess { categoryProducts ->
                    _uiState.value = CategoryUiState.Success(
                        categories = categories,
                        selectedCategory = category,
                        categoryProducts = categoryProducts
                    )
                }
                .onFailure { error ->
                    _uiState.value = CategoryUiState.Error(error.message ?: "Failed to load category products")
                }
        }
    }
}
