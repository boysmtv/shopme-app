package com.mtv.app.shopme.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class PaymentStatus {
    UNPAID,
    WAITING_UPLOAD,
    WAITING_CONFIRMATION,
    PAID,
    FAILED
}
