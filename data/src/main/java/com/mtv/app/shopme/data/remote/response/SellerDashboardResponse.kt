package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class SellerDashboardResponse(
    val totalRevenue: String = "Rp 0",
    val todayRevenue: String = "Rp 0",
    val thisWeekRevenue: String = "Rp 0",
    val thisMonthRevenue: String = "Rp 0",
    val totalOrders: Long = 0,
    val pendingOrders: Long = 0,
    val processingOrders: Long = 0,
    val completedOrders: Long = 0,
    val cancelledOrders: Long = 0,
    val totalProducts: Long = 0,
    val activeProducts: Long = 0,
    val lowStockProducts: Long = 0,
    val totalSold: Long = 0,
    val weeklyRevenue: List<ChartDataPoint> = emptyList(),
    val monthlyRevenue: List<ChartDataPoint> = emptyList(),
    val orderStatusBreakdown: List<StatusCount> = emptyList(),
    val topProducts: List<TopProductData> = emptyList()
)

@Serializable
data class ChartDataPoint(
    val label: String = "",
    val value: String = "0"
)

@Serializable
data class StatusCount(
    val status: String = "",
    val count: Long = 0
)

@Serializable
data class TopProductData(
    val productId: String? = null,
    val productName: String = "",
    val totalSold: Long = 0,
    val revenue: String = "Rp 0",
    val price: String = "Rp 0"
)
