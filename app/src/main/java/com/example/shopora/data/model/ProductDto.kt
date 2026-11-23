package com.example.shopora.data.model

/**
 * @author Utkarsh Srivastava
 */

import com.google.gson.annotations.SerializedName

data class RatingDto(
    @SerializedName("rate") val rate: Double,
    @SerializedName("count") val count: Int
)

data class ProductDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("price") val price: Double,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("image") val image: String,
    @SerializedName("rating") val rating: RatingDto?
)

data class CategoryProductsDto(
    @SerializedName("category") val category: String,
    @SerializedName("topRated") val topRated: List<ProductDto>,
    @SerializedName("allProducts") val allProducts: List<ProductDto>
)

data class EnquiryRequestDto(
    @SerializedName("productId") val productId: Int,
    @SerializedName("productTitle") val productTitle: String,
    @SerializedName("customerName") val customerName: String,
    @SerializedName("customerEmail") val customerEmail: String,
    @SerializedName("message") val message: String
)

data class EnquiryResponseDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("enquiryId") val enquiryId: String?,
    @SerializedName("timestamp") val timestamp: String?
)
