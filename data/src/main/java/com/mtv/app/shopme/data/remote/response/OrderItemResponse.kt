package com.mtv.app.shopme.data.remote.response

import com.mtv.app.shopme.common.serializer.BigDecimalSerializer
import com.mtv.app.shopme.domain.model.OrderItemStatus
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class OrderItemResponse(
    val id: String,
    val foodId: String,
    val foodName: String? = null,
    val quantity: Int,
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal,
    val notes: String? = null,
    val status: OrderItemStatus
)
