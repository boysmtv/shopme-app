package com.mtv.app.shopme.domain.model

data class OrderTimeline(
    val status: OrderStatus,
    val actorRole: String = "",
    val reason: String = "",
    val createdAt: String = ""
)
