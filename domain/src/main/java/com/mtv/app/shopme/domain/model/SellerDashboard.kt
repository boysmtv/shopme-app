package com.mtv.app.shopme.domain.model

data class SellerDashboard(
    val totalRevenue: String,
    val todayRevenue: String,
    val thisWeekRevenue: String,
    val thisMonthRevenue: String,
    val totalOrders: Long,
    val pendingOrders: Long,
    val processingOrders: Long,
    val completedOrders: Long,
    val cancelledOrders: Long,
    val totalProducts: Long,
    val activeProducts: Long,
    val lowStockProducts: Long,
    val totalSold: Long,
    val weeklyRevenue: List<ChartDataItem>,
    val monthlyRevenue: List<ChartDataItem>,
    val orderStatusBreakdown: List<StatusCountItem>,
    val topProducts: List<TopProductItem>
)

data class ChartDataItem(
    val label: String,
    val value: Long
)

data class StatusCountItem(
    val status: String,
    val count: Long
)

data class TopProductItem(
    val productId: String?,
    val productName: String,
    val totalSold: Long,
    val revenue: String,
    val price: String
)
