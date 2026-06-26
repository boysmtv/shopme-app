package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.domain.model.OrderStatus

enum class OrderFilter(val label: String) {
    SEMUA("Semua"),
    ORDERED("Dipesan"),
    COOKING("Dimasak"),
    DELIVERING("Dikirim"),
    COMPLETED("Selesai"),
    CANCELLED("Batal"),
    DIBATALKAN("Dibatalkan");

    fun matches(status: String): Boolean = when (this) {
        SEMUA -> true
        ORDERED -> status == "BELUM DIBAYAR" || status == "DIPESAN"
        COOKING -> status == "DIMASAK"
        DELIVERING -> status == "DIKIRIM"
        COMPLETED -> status == "SELESAI"
        CANCELLED -> status == "BATAL"
        DIBATALKAN -> status == "DIBATALKAN"
    }
}

fun orderStatusToFilterLabel(status: String): String = when (status) {
    "BELUM DIBAYAR" -> "Belum Dibayar"
    "DIPESAN" -> "Dipesan"
    "DIMASAK" -> "Dimasak"
    "DIKIRIM" -> "Dikirim"
    "SELESAI" -> "Selesai"
    "BATAL" -> "Batal"
    "DIBATALKAN" -> "Dibatalkan"
    else -> status
}

fun resolveOrderFilter(name: String): OrderFilter =
    OrderFilter.entries.firstOrNull { it.name == name.uppercase() } ?: OrderFilter.SEMUA

fun filterOrders(
    orders: List<com.mtv.app.shopme.domain.model.Order>,
    selectedFilter: OrderFilter
): List<com.mtv.app.shopme.domain.model.Order> =
    orders.filter {
        when (selectedFilter) {
            OrderFilter.SEMUA -> true
            OrderFilter.ORDERED -> it.status == OrderStatus.UNPAID || it.status == OrderStatus.ORDERED
            OrderFilter.COOKING -> it.status == OrderStatus.COOKING
            OrderFilter.DELIVERING -> it.status == OrderStatus.DELIVERING
            OrderFilter.COMPLETED -> it.status == OrderStatus.COMPLETED
            OrderFilter.CANCELLED -> it.status == OrderStatus.CANCELLED
            OrderFilter.DIBATALKAN -> it.status == OrderStatus.CANCELLED
        }
    }
