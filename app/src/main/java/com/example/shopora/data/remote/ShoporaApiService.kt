package com.example.shopora.data.remote

/**
 * @author Utkarsh Srivastava
 */

import com.example.shopora.data.model.CategoryProductsDto
import com.example.shopora.data.model.EnquiryRequestDto
import com.example.shopora.data.model.EnquiryResponseDto
import com.example.shopora.data.model.ProductDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ShoporaApiService {

    @GET("api/products/featured")
    suspend fun getFeaturedProducts(): List<ProductDto>

    @GET("api/products")
    suspend fun getAllProducts(): List<ProductDto>

    @GET("api/products/categories")
    suspend fun getCategories(): List<String>

    @GET("api/products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): CategoryProductsDto

    @GET("api/products/search")
    suspend fun searchProducts(@Query("q") query: String): List<ProductDto>

    @GET("api/products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<ProductDto>

    @POST("api/enquiries")
    suspend fun submitEnquiry(@Body enquiry: EnquiryRequestDto): Response<EnquiryResponseDto>
}
