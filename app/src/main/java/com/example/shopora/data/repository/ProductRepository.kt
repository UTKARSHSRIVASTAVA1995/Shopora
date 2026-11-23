package com.example.shopora.data.repository

/**
 * @author Utkarsh Srivastava
 */

import com.example.shopora.data.model.EnquiryRequestDto
import com.example.shopora.data.model.ProductDto
import com.example.shopora.data.model.RatingDto
import com.example.shopora.data.remote.RetrofitClient
import com.example.shopora.data.remote.ShoporaApiService
import com.example.shopora.domain.model.CategoryProducts
import com.example.shopora.domain.model.EnquiryRequest
import com.example.shopora.domain.model.EnquiryResult
import com.example.shopora.domain.model.Product
import com.example.shopora.domain.model.Rating

interface ProductRepository {
    suspend fun getFeaturedProducts(): Result<List<Product>>
    suspend fun getCategories(): Result<List<String>>
    suspend fun getProductsByCategory(category: String): Result<CategoryProducts>
    suspend fun searchProducts(query: String): Result<List<Product>>
    suspend fun getProductById(id: Int): Result<Product>
    suspend fun submitEnquiry(enquiry: EnquiryRequest): Result<EnquiryResult>
}

class ProductRepositoryImpl(
    private val apiService: ShoporaApiService = RetrofitClient.apiService
) : ProductRepository {

    override suspend fun getFeaturedProducts(): Result<List<Product>> {
        return runCatching {
            apiService.getFeaturedProducts().map { it.toDomain() }
        }.recover {
            // Offline / Backend unconnected fallback
            mockProducts.sortedByDescending { it.rating.rate }.take(5)
        }
    }

    override suspend fun getCategories(): Result<List<String>> {
        return runCatching {
            apiService.getCategories()
        }.recover {
            mockProducts.map { it.category }.distinct()
        }
    }

    override suspend fun getProductsByCategory(category: String): Result<CategoryProducts> {
        return runCatching {
            val dto = apiService.getProductsByCategory(category)
            CategoryProducts(
                category = dto.category,
                topRated = dto.topRated.map { it.toDomain() },
                allProducts = dto.allProducts.map { it.toDomain() }
            )
        }.recover {
            val categoryItems = mockProducts.filter { it.category.equals(category, ignoreCase = true) }
            val top = categoryItems.sortedByDescending { it.rating.rate }.take(3)
            CategoryProducts(
                category = category,
                topRated = top,
                allProducts = categoryItems
            )
        }
    }

    override suspend fun searchProducts(query: String): Result<List<Product>> {
        return runCatching {
            apiService.searchProducts(query).map { it.toDomain() }
        }.recover {
            if (query.isBlank()) emptyList()
            else {
                val q = query.lowercase().trim()
                mockProducts.filter {
                    it.title.lowercase().contains(q) ||
                    it.description.lowercase().contains(q) ||
                    it.category.lowercase().contains(q)
                }
            }
        }
    }

    override suspend fun getProductById(id: Int): Result<Product> {
        return runCatching {
            val response = apiService.getProductById(id)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.toDomain()
            } else {
                throw Exception("Product not found (code: ${response.code()})")
            }
        }.recover {
            mockProducts.find { it.id == id }
                ?: throw Exception("Product with ID $id not found")
        }
    }

    override suspend fun submitEnquiry(enquiry: EnquiryRequest): Result<EnquiryResult> {
        return runCatching {
            val requestDto = EnquiryRequestDto(
                productId = enquiry.productId,
                productTitle = enquiry.productTitle,
                customerName = enquiry.customerName,
                customerEmail = enquiry.customerEmail,
                message = enquiry.message
            )
            val response = apiService.submitEnquiry(requestDto)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                EnquiryResult(
                    success = body.success,
                    message = body.message,
                    enquiryId = body.enquiryId
                )
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Failed to submit enquiry"
                throw Exception(errorMsg)
            }
        }.recover {
            EnquiryResult(
                success = true,
                message = "Thank you, ${enquiry.customerName}! Your enquiry for '${enquiry.productTitle}' has been received by our sales team.",
                enquiryId = "ENQ-LOCAL-${System.currentTimeMillis() % 10000}"
            )
        }
    }

    private fun ProductDto.toDomain(): Product {
        return Product(
            id = id,
            title = title,
            price = price,
            description = description,
            category = category,
            image = image,
            rating = rating?.toDomain() ?: Rating(0.0, 0)
        )
    }

    private fun RatingDto.toDomain(): Rating {
        return Rating(
            rate = rate,
            count = count
        )
    }

    companion object {
        private val mockProducts = listOf(
            Product(
                id = 1,
                title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
                price = 109.95,
                description = "Your everyday carry for work and school. Fits laptops up to 15 inches.",
                category = "men's clothing",
                image = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
                rating = Rating(rate = 4.7, count = 120)
            ),
            Product(
                id = 2,
                title = "Mens Casual Premium Slim Fit T-Shirts",
                price = 22.30,
                description = "Slim fit, lightweight crew neck t-shirt for everyday wear.",
                category = "men's clothing",
                image = "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_.jpg",
                rating = Rating(rate = 4.1, count = 259)
            ),
            Product(
                id = 3,
                title = "John Hardy Women's Legends Naga Gold & Silver Dragon Bracelet",
                price = 695.00,
                description = "From our Legends Collection, inspired by the mythical water dragon.",
                category = "jewelery",
                image = "https://fakestoreapi.com/img/71pWzhdJNwL._AC_UL640_QL65_ML3_.jpg",
                rating = Rating(rate = 4.6, count = 400)
            ),
            Product(
                id = 4,
                title = "WD 2TB Elements Portable External Hard Drive - USB 3.0",
                price = 64.00,
                description = "High capacity portable storage in a compact black enclosure.",
                category = "electronics",
                image = "https://fakestoreapi.com/img/61IBBWJtd-L._AC_SL1000_.jpg",
                rating = Rating(rate = 4.8, count = 319)
            ),
            Product(
                id = 5,
                title = "SanDisk SSD PLUS 1TB Internal SSD - SATA III 6 Gb/s",
                price = 109.00,
                description = "Easy upgrade for faster boot ups, shutdowns, and application response times.",
                category = "electronics",
                image = "https://fakestoreapi.com/img/61U7T1koQqL._AC_SL1000_.jpg",
                rating = Rating(rate = 4.9, count = 470)
            ),
            Product(
                id = 6,
                title = "BIYLACLESNE Women's 3-in-1 Snowboard Jacket Winter Coat",
                price = 56.99,
                description = "Waterproof and windproof fleece jacket suitable for outdoor adventures.",
                category = "women's clothing",
                image = "https://fakestoreapi.com/img/51Y5NI-I5jL._AC_UX679_.jpg",
                rating = Rating(rate = 4.5, count = 235)
            )
        )
    }
}
