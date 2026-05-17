package com.mtv.app.shopme.common.notification

object NotificationDeepLinkExtras {
    const val TYPE = "shopme_notification_type"
    const val ORDER_ID = "shopme_order_id"
    const val CONVERSATION_ID = "shopme_conversation_id"
    const val ROLE = "shopme_role"
}

data class NotificationDeepLink(
    val type: String,
    val orderId: String = "",
    val conversationId: String = "",
    val role: String = ""
) {
    val isOrder: Boolean
        get() = orderId.isNotBlank() && type.lowercase() in ORDER_TYPES

    val isChat: Boolean
        get() = conversationId.isNotBlank() && type.lowercase() in CHAT_TYPES

    companion object {
        private val ORDER_TYPES = setOf(
            "order_created",
            "payment_confirmation",
            "order_status_updated",
            "order_cancelled",
            "order_canceled",
            "order"
        )
        private val CHAT_TYPES = setOf("chat_message", "chat")

        fun from(data: Map<String, String>): NotificationDeepLink? {
            val type = data.firstValue("type", NotificationDeepLinkExtras.TYPE)
                .ifBlank {
                    when {
                        data.firstValue("conversationId", "conversation_id", NotificationDeepLinkExtras.CONVERSATION_ID).isNotBlank() -> "chat"
                        data.firstValue("orderId", "order_id", NotificationDeepLinkExtras.ORDER_ID).isNotBlank() -> "order"
                        else -> ""
                    }
                }
                .ifBlank { return null }

            val deepLink = NotificationDeepLink(
                type = type,
                orderId = data.firstValue("orderId", "order_id", NotificationDeepLinkExtras.ORDER_ID),
                conversationId = data.firstValue(
                    "conversationId",
                    "conversation_id",
                    "chatId",
                    "chat_id",
                    NotificationDeepLinkExtras.CONVERSATION_ID
                ),
                role = data.firstValue(
                    "role",
                    "targetRole",
                    "userRole",
                    NotificationDeepLinkExtras.ROLE
                )
            )
            return deepLink.takeIf { it.isOrder || it.isChat }
        }
    }
}

private fun Map<String, String>.firstValue(vararg keys: String): String {
    return keys.firstNotNullOfOrNull { key -> get(key)?.takeIf { it.isNotBlank() } }.orEmpty()
}
