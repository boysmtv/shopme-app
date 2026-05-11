package com.mtv.app.shopme.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class OrderItemStatus {
    AVAILABLE,
    NOT_AVAILABLE
}
