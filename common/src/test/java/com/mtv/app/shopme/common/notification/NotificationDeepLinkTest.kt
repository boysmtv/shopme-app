package com.mtv.app.shopme.common.notification

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NotificationDeepLinkTest {

    @Test
    fun `from returns order deep link from backend metadata`() {
        val deepLink = NotificationDeepLink.from(
            mapOf(
                "type" to "order_status_updated",
                "orderId" to "order-1",
                "targetRole" to "customer"
            )
        )

        assertEquals("order_status_updated", deepLink?.type)
        assertEquals("order-1", deepLink?.orderId)
        assertEquals("customer", deepLink?.role)
    }

    @Test
    fun `from returns chat deep link from conversation metadata`() {
        val deepLink = NotificationDeepLink.from(
            mapOf(
                "type" to "chat_message",
                "conversationId" to "conv-1"
            )
        )

        assertEquals("conv-1", deepLink?.conversationId)
    }

    @Test
    fun `from ignores notification without target id`() {
        assertNull(NotificationDeepLink.from(mapOf("type" to "order_status_updated")))
    }
}
