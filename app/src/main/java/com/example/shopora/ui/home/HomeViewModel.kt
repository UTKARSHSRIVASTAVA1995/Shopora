package com.example.shopora.ui.home

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

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val featuredProducts: List<Product>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

class HomeViewModel(
    private val repository: ProductRepository = ProductRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadFeaturedProducts()
    }

    fun loadFeaturedProducts() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            repository.getFeaturedProducts()
                .onSuccess { products ->
                    _uiState.value = HomeUiState.Success(products)
                }
                .onFailure { error ->
                    _uiState.value = HomeUiState.Error(error.message ?: "Failed to load featured products")
                }
        }
    }
}
