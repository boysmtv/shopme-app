package com.mtv.app.shopme.data.remote.response

import com.mtv.app.shopme.common.serializer.BigDecimalSerializer
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.model.PaymentMethod
import com.mtv.app.shopme.domain.model.PaymentStatus
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class OrderSummaryResponse(
    val id: String,
    val cafeId: String,
    val cafeName: String? = null,
    val deliveryAddress: String? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val totalPrice: BigDecimal,
    val status: OrderStatus,
    val paymentMethod: PaymentMethod,
    val paymentStatus: PaymentStatus,
    val createdAt: String? = null,
    val itemCount: Int = 0,
    val items: List<OrderSummaryItemResponse> = emptyList()
)

@Serializable
data class OrderSummaryItemResponse(
    val foodName: String? = null,
    val quantity: Int
)

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
    val items: List<OrderItemResponse>,
    val timeline: List<OrderTimelineResponse> = emptyList(),
    val payment: OrderPaymentResponse? = null
)

@Serializable
data class OrderTimelineResponse(
    val status: OrderStatus,
    val actorRole: String = "",
    val reason: String? = null,
    val createdAt: String = ""
)

@Serializable
data class OrderPaymentResponse(
    val method: PaymentMethod,
    val status: PaymentStatus,
    val transferConfirmationAvailable: Boolean = false,
    val sellerVerificationRequired: Boolean = false,
    val proofRequired: Boolean = false,
    val proofUrl: String? = null,
    val instruction: String = "",
    val history: List<OrderPaymentHistoryResponse> = emptyList()
)

@Serializable
data class OrderPaymentHistoryResponse(
    val status: PaymentStatus,
    val actorRole: String = "",
    val reason: String? = null,
    val createdAt: String = ""
)
