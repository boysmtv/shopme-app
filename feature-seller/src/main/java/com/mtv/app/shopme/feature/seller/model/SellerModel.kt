/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerModel.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 12.02
 */

package com.mtv.app.shopme.feature.seller.model

import com.mtv.app.shopme.data.OrderStatus

data class SellerDashboard(
    val todayRevenue: Double,
    val todayOrders: Int,
    val pendingOrders: Int,
    val isStoreOpen: Boolean,
    val recentOrders: List<OrderSummary>
)

data class OrderSummary(
    val orderId: String,
    val customerName: String,
    val totalPrice: Double,
    val status: OrderStatus
)

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val basePrice: Double,
    val imageUrl: String,
    val categoryId: String,
    val isActive: Boolean,
    val variants: List<VariantGroup>,
    val stock: Int?
)

data class VariantGroup(
    val id: String,
    val name: String, // contoh: Ukuran
    val type: VariantType,
    val options: List<VariantOption>
)

enum class VariantType {
    SINGLE, // radio
    MULTIPLE // checkbox
}

data class VariantOption(
    val id: String,
    val name: String,
    val additionalPrice: Double
)

data class Order(
    val id: String,
    val customerId: String,
    val items: List<OrderItem>,
    val status: OrderStatus,
    val totalPrice: Double,
    val note: String?,
    val createdAt: Long
)

data class OrderItem(
    val productId: String,
    val productName: String,
    val selectedVariants: List<SelectedVariant>,
    val quantity: Int,
    val price: Double
)

data class SelectedVariant(
    val groupName: String,
    val optionName: String
)

data class StoreProfile(
    val id: String,
    val name: String,
    val description: String,
    val logoUrl: String,
    val bannerUrl: String,
    val address: String,
    val phone: String,
    val openTime: String,
    val closeTime: String,
    val isOpen: Boolean
)

data class StoreState(
    val storeName: String = "",
    val description: String = "",
    val address: String = "",
    val phone: String = "",
    val openTime: String = "08:00",
    val closeTime: String = "22:00",
    val isOpen: Boolean = true,
    val logoUrl: String = "",
    val bannerUrl: String = "",
    val isLoading: Boolean = false
)

data class SalesAnalytics(
    val totalRevenue: Double,
    val totalOrders: Int,
    val bestSellingProducts: List<BestSellingProduct>
)

data class BestSellingProduct(
    val productId: String,
    val productName: String,
    val totalSold: Int
)

data class Promotion(
    val id: String,
    val title: String,
    val type: PromotionType,
    val value: Double,
    val minPurchase: Double,
    val expiredAt: Long,
    val isActive: Boolean
)

enum class PromotionType {
    PERCENTAGE,
    FIXED
}

data class SellerOrderModel(
    val id: String,
    val customerName: String,
    val total: String,
    val status: OrderStatus
)

val dummyOrders = listOf(
    SellerOrderModel("INV-001", "Dedy Wijaya", "Rp 120.000", OrderStatus.ORDERED),
    SellerOrderModel("INV-002", "Budi Santoso", "Rp 85.000", OrderStatus.COOKING),
    SellerOrderModel("INV-003", "Sari Dewi", "Rp 210.000", OrderStatus.DELIVERING),
    SellerOrderModel("INV-003", "Dika Pratama", "Rp 210.000", OrderStatus.COMPLETED),
)

data class SellerProduct(
    val id: String,
    val name: String,
    val price: String,
    val stock: Int,
    val imageUrl: String? = null
)
