package com.example.shopora

/**
 * @author Utkarsh Srivastava
 */

import com.example.shopora.data.model.ProductDto
import com.example.shopora.data.model.RatingDto
import com.example.shopora.data.remote.ShoporaApiService
import com.example.shopora.data.repository.ProductRepositoryImpl
import com.example.shopora.domain.model.EnquiryRequest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class FakeShoporaApiService : ShoporaApiService {
    var shouldReturnError = false

    val dummyProducts = listOf(
        ProductDto(
            id = 1,
            title = "Test Phone",
            price = 999.99,
            description = "High end test smartphone",
            category = "electronics",
            image = "https://example.com/phone.jpg",
            rating = RatingDto(rate = 4.8, count = 120)
        ),
        ProductDto(
            id = 2,
            title = "Test Shirt",
            price = 29.99,
            description = "Cotton t-shirt",
            category = "clothing",
            image = "https://example.com/shirt.jpg",
            rating = RatingDto(rate = 4.2, count = 50)
        )
    )

    override suspend fun getFeaturedProducts(): List<ProductDto> {
        if (shouldReturnError) throw Exception("Network Error")
        return dummyProducts
    }

    override suspend fun getAllProducts(): List<ProductDto> {
        if (shouldReturnError) throw Exception("Network Error")
        return dummyProducts
    }

    override suspend fun getCategories(): List<String> {
        if (shouldReturnError) throw Exception("Network Error")
        return listOf("electronics", "clothing")
    }

    override suspend fun getProductsByCategory(category: String): com.example.shopora.data.model.CategoryProductsDto {
        if (shouldReturnError) throw Exception("Network Error")
        val categoryItems = dummyProducts.filter { it.category == category }
        return com.example.shopora.data.model.CategoryProductsDto(
            category = category,
            topRated = categoryItems,
            allProducts = categoryItems
        )
    }

    override suspend fun searchProducts(query: String): List<ProductDto> {
        if (shouldReturnError) throw Exception("Network Error")
        return dummyProducts.filter { it.title.contains(query, ignoreCase = true) }
    }

    override suspend fun getProductById(id: Int): Response<ProductDto> {
        if (shouldReturnError) throw Exception("Network Error")
        val product = dummyProducts.find { it.id == id }
        return if (product != null) Response.success(product) else Response.error(404, okhttp3.ResponseBody.create(null, "Not Found"))
    }

    override suspend fun submitEnquiry(enquiry: com.example.shopora.data.model.EnquiryRequestDto): Response<com.example.shopora.data.model.EnquiryResponseDto> {
        if (shouldReturnError) throw Exception("Network Error")
        return Response.success(
            com.example.shopora.data.model.EnquiryResponseDto(
                success = true,
                message = "Thank you!",
                enquiryId = "ENQ-123",
                timestamp = "2026-02-22T00:00:00Z"
            )
        )
    }
}

class ProductRepositoryTest {

    @Test
    fun getFeaturedProducts_returnsMappedDomainModels() = runTest {
        val fakeApi = FakeShoporaApiService()
        val repository = ProductRepositoryImpl(fakeApi)

        val result = repository.getFeaturedProducts()
        assertTrue(result.isSuccess)
        val products = result.getOrNull()!!
        assertEquals(2, products.size)
        assertEquals("Test Phone", products[0].title)
        assertEquals(4.8, products[0].rating.rate, 0.01)
    }

    @Test
    fun getProductById_returnsProduct_whenFound() = runTest {
        val fakeApi = FakeShoporaApiService()
        val repository = ProductRepositoryImpl(fakeApi)

        val result = repository.getProductById(1)
        assertTrue(result.isSuccess)
        assertEquals("Test Phone", result.getOrNull()?.title)
    }

    @Test
    fun submitEnquiry_returnsSuccessResult() = runTest {
        val fakeApi = FakeShoporaApiService()
        val repository = ProductRepositoryImpl(fakeApi)

        val enquiry = EnquiryRequest(
            productId = 1,
            productTitle = "Test Phone",
            customerName = "Jane Doe",
            customerEmail = "jane@example.com",
            message = "Interested in bulk pricing."
        )

        val result = repository.submitEnquiry(enquiry)
        assertTrue(result.isSuccess)
        assertEquals(true, result.getOrNull()?.success)
        assertEquals("ENQ-123", result.getOrNull()?.enquiryId)
    }
}
