package com.mtv.app.shopme.data.remote.response

import com.mtv.app.shopme.common.serializer.BigDecimalSerializer
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.model.PaymentMethod
import com.mtv.app.shopme.domain.model.PaymentStatus
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class OrderResponse(
    val id: String,
    val customerId: String,
    val cafeId: String,
    @Serializable(with = BigDecimalSerializer::class)
    val totalPrice: BigDecimal,
    val status: OrderStatus,
    val paymentMethod: PaymentMethod,
    val paymentStatus: PaymentStatus,
    val items: List<OrderItemResponse>
)