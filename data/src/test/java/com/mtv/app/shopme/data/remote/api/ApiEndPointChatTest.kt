package com.mtv.app.shopme.data.remote.api

import org.junit.Assert.assertEquals
import org.junit.Test

class ApiEndPointChatTest {

    @Test
    fun `ensure conversation endpoints should have correct paths`() {
        assertEquals(
            "api/v1/chat/conversation",
            ApiEndPoint.Chat.EnsureConversation.path
        )
        assertEquals(
            "api/v1/chat/conversation/order",
            ApiEndPoint.Chat.EnsureOrderConversation.path
        )
        assertEquals(
            "api/v1/chat/conversation/seller",
            ApiEndPoint.Chat.EnsureSellerConversation.path
        )
    }
}
