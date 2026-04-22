package com.mtv.app.shopme.domain.model

enum class PaymentStatus {
    UNPAID,
    WAITING_UPLOAD,
    WAITING_CONFIRMATION,
    PAID,
    FAILED
}