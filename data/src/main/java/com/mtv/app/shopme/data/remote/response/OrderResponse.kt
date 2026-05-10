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
    val customerName: String? = null,
    val cafeId: String,
    val cafeName: String? = null,
    val deliveryAddress: String? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val totalPrice: BigDecimal,
    val status: OrderStatus,
    val paymentMethod: PaymentMethod,
    val paymentStatus: PaymentStatus,
    val createdAt: String? = null,
    val items: List<OrderItemResponse>
)
