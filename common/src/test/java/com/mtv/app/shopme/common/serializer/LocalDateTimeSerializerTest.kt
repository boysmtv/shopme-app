package com.mtv.app.shopme.common.serializer

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.LocalDateTime

class LocalDateTimeSerializerTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `deserialize should support local datetime`() {
        val payload = """{"value":"2026-05-11T10:15:30"}"""

        val decoded = json.decodeFromString<Wrapper>(payload)

        assertEquals(LocalDateTime.of(2026, 5, 11, 10, 15, 30), decoded.value)
    }

    @Test
    fun `deserialize should support utc offset datetime`() {
        val payload = """{"value":"2026-05-11T10:15:30Z"}"""

        val decoded = json.decodeFromString<Wrapper>(payload)

        assertEquals(LocalDateTime.of(2026, 5, 11, 10, 15, 30), decoded.value)
    }

    @Serializable
    private data class Wrapper(
        @Serializable(with = LocalDateTimeSerializer::class)
        val value: LocalDateTime
    )
}
