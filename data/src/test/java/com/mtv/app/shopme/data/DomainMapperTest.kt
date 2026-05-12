package com.mtv.app.shopme.data

import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.remote.response.CartItemResponse
import java.math.BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DomainMapperTest {

    @Test
    fun `cart item response should tolerate nullable note image and variants`() {
        val response = CartItemResponse(
            id = "cart-1",
            name = "Es Teh Manis",
            image = null,
            quantity = 1,
            notes = null,
            cafeId = "cafe-1",
            cafeName = "Kopi Tugu Senja",
            foodId = "food-1",
            customerId = "customer-1",
            price = BigDecimal("12000"),
            variants = null
        )

        val result = response.toDomain()

        assertEquals("", result.image)
        assertEquals("", result.notes)
        assertTrue(result.variants.isEmpty())
    }
}
