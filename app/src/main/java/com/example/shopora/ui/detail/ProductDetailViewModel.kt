package com.example.shopora.ui.detail

/**
 * @author Utkarsh Srivastava
 */

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopora.data.repository.ProductRepository
import com.example.shopora.data.repository.ProductRepositoryImpl
import com.example.shopora.domain.model.EnquiryRequest
import com.example.shopora.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ProductDetailUiState {
    object Loading : ProductDetailUiState
    data class Success(val product: Product) : ProductDetailUiState
    data class Error(val message: String) : ProductDetailUiState
}

sealed interface EnquiryUiState {
    object Idle : EnquiryUiState
    object Submitting : EnquiryUiState
    data class Success(val message: String, val enquiryId: String?) : EnquiryUiState
    data class Error(val message: String) : EnquiryUiState
}

class ProductDetailViewModel(
    private val repository: ProductRepository = ProductRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private val _enquiryState = MutableStateFlow<EnquiryUiState>(EnquiryUiState.Idle)
    val enquiryState: StateFlow<EnquiryUiState> = _enquiryState.asStateFlow()

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            _uiState.value = ProductDetailUiState.Loading
            repository.getProductById(productId)
                .onSuccess { product ->
                    _uiState.value = ProductDetailUiState.Success(product)
                }
                .onFailure { error ->
                    _uiState.value = ProductDetailUiState.Error(error.message ?: "Failed to load product details")
                }
        }
    }

    fun submitEnquiry(customerName: String, customerEmail: String, message: String) {
        val product = (_uiState.value as? ProductDetailUiState.Success)?.product ?: return

        viewModelScope.launch {
            _enquiryState.value = EnquiryUiState.Submitting
            val enquiry = EnquiryRequest(
                productId = product.id,
                productTitle = product.title,
                customerName = customerName,
                customerEmail = customerEmail,
                message = message
            )
            repository.submitEnquiry(enquiry)
                .onSuccess { result ->
                    _enquiryState.value = EnquiryUiState.Success(result.message, result.enquiryId)
                }
                .onFailure { error ->
                    _enquiryState.value = EnquiryUiState.Error(error.message ?: "Failed to submit enquiry")
                }
        }
    }

    fun resetEnquiryState() {
        _enquiryState.value = EnquiryUiState.Idle
    }
}
