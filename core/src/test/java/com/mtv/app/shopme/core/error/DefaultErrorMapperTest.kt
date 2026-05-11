package com.mtv.app.shopme.core.error

import com.mtv.based.core.network.utils.UiError
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultErrorMapperTest {

    private val mapper = DefaultErrorMapper()

    @Test
    fun `map should preserve forbidden message from backend`() {
        val result = mapper.map(
            ApiException.Forbidden(
                statusCode = 403,
                errorCode = "FORBIDDEN",
                message = "Access Denied"
            )
        )

        assertTrue(result is UiError.Forbidden)
        assertEquals("Access Denied", result.message)
    }

    @Test
    fun `map should preserve conflict message from backend`() {
        val result = mapper.map(
            ApiException.Conflict(
                statusCode = 409,
                errorCode = "CONFLICT",
                message = "Village is still referenced by other data"
            )
        )

        assertTrue(result is UiError.Validation)
        assertEquals("Village is still referenced by other data", result.message)
    }

    @Test
    fun `map should preserve server message from backend`() {
        val result = mapper.map(
            ApiException.ServerError(
                statusCode = 500,
                errorCode = "INTERNAL_ERROR",
                message = "Village is still referenced by other data"
            )
        )

        assertTrue(result is UiError.Server)
        assertEquals("Village is still referenced by other data", result.message)
    }

    @Test
    fun `map should preserve unknown message when provided`() {
        val result = mapper.map(
            ApiException.Unknown(
                statusCode = 418,
                errorCode = "TEAPOT",
                message = "Custom failure"
            )
        )

        assertTrue(result is UiError.Unknown)
        assertEquals("Custom failure", result.message)
    }
}
