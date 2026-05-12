package com.mtv.app.shopme.core.realtime

enum class ShopmeRealtimeEventType {
    CONNECTED,
    CHAT_MESSAGE,
    CHAT_READ,
    NOTIFICATION_CREATED,
    UNKNOWN;

    companion object {
        fun from(raw: String?): ShopmeRealtimeEventType = entries.firstOrNull { it.name == raw } ?: UNKNOWN
    }
}

data class ShopmeRealtimeEvent(
    val type: ShopmeRealtimeEventType,
    val conversationId: String? = null,
    val customerId: String? = null,
    val cafeId: String? = null,
    val notificationId: String? = null,
    val title: String? = null,
    val message: String? = null,
    val actorId: String? = null,
    val occurredAt: String? = null
)
