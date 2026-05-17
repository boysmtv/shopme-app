package com.mtv.app.shopme.data

import com.mtv.app.shopme.core.realtime.ShopmeRealtimeEventType
import com.mtv.app.shopme.data.realtime.buildRealtimeUrl
import com.mtv.app.shopme.data.realtime.parseRealtimeEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ShopmeRealtimeGatewayImplTest {

    @Test
    fun `buildRealtimeUrl should convert http base url into websocket endpoint`() {
        val actual = buildRealtimeUrl(
            baseUrl = "http://192.168.100.20:8080/",
            token = "jwt token"
        )

        assertEquals(
            "ws://192.168.100.20:8080/ws/realtime?token=jwt+token",
            actual
        )
    }

    @Test
    fun `parseRealtimeEvent should decode backend payload`() {
        val event = parseRealtimeEvent(
            """{"type":"CHAT_MESSAGE","conversationId":"conv-1","customerId":"cust-1","cafeId":"cafe-1","title":"Kopi Kita","message":"Halo buyer","actorId":"seller-1","occurredAt":"2026-05-11T21:00:00Z"}"""
        )

        assertNotNull(event)
        assertEquals(ShopmeRealtimeEventType.CHAT_MESSAGE, event?.type)
        assertEquals("conv-1", event?.conversationId)
        assertEquals("cust-1", event?.customerId)
        assertEquals("cafe-1", event?.cafeId)
        assertEquals("Kopi Kita", event?.title)
        assertEquals("Halo buyer", event?.message)
    }

    @Test
    fun `parseRealtimeEvent should decode presence payload`() {
        val event = parseRealtimeEvent(
            """{"type":"PRESENCE_CHANGED","onlineCafeId":"cafe-1","online":true,"lastSeenAt":"2026-05-17T01:00:00Z","occurredAt":"2026-05-17T01:00:00Z"}"""
        )

        assertNotNull(event)
        assertEquals(ShopmeRealtimeEventType.PRESENCE_CHANGED, event?.type)
        assertEquals("cafe-1", event?.onlineCafeId)
        assertEquals(true, event?.online)
        assertEquals("2026-05-17T01:00:00Z", event?.lastSeenAt)
    }

    @Test
    fun `parseRealtimeEvent should ignore invalid payload`() {
        val event = parseRealtimeEvent("not-json")

        assertTrue(event == null)
    }
}
