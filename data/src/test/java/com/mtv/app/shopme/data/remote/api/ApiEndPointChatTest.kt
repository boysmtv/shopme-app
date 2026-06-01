package com.mtv.app.shopme.data.remote.api

import org.junit.Assert.assertEquals
import org.junit.Test

class ApiEndPointChatTest {

    @Test
    fun `ensure conversation endpoints should encode identifiers in path query`() {
        assertEquals(
            "api/chat/conversation?cafeId=cafe-1",
            ApiEndPoint.Chat.EnsureConversation("cafe-1").path
        )
        assertEquals(
            "api/chat/conversation/order?orderId=order-1",
            ApiEndPoint.Chat.EnsureOrderConversation("order-1").path
        )
        assertEquals(
            "api/chat/conversation/seller?orderId=order-1",
            ApiEndPoint.Chat.EnsureSellerConversation("order-1").path
        )
    }
}
