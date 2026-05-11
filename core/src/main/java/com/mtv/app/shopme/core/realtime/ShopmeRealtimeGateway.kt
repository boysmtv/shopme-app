package com.mtv.app.shopme.core.realtime

import kotlinx.coroutines.flow.Flow

interface ShopmeRealtimeGateway {
    val events: Flow<ShopmeRealtimeEvent>

    fun ensureConnected()
}
