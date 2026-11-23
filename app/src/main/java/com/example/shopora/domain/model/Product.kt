package com.example.shopora.domain.model

/**
 * @author Utkarsh Srivastava
 */

data class Rating(
    val rate: Double,
    val count: Int
)

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating
)

data class CategoryProducts(
    val category: String,
    val topRated: List<Product>,
    val allProducts: List<Product>
)

data class EnquiryRequest(
    val productId: Int,
    val productTitle: String,
    val customerName: String,
    val customerEmail: String,
    val message: String
)

data class EnquiryResult(
    val success: Boolean,
    val message: String,
    val enquiryId: String? = null
)
