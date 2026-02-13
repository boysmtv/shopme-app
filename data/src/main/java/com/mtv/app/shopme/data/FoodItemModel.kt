/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: FoodItemModel.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 15.29
 */

package com.mtv.app.shopme.data

import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

data class FoodItemModel(
    val id: Int = 0,
    val name: String = EMPTY_STRING,
    val desc: String = EMPTY_STRING,
    val price: Double = 0.0,
    val imageUrl: String = EMPTY_STRING,
    val cafeId: String = EMPTY_STRING,
    val categoryId: String = EMPTY_STRING
)

data class OwnerCafeModel(
    val ownerId: String = EMPTY_STRING,
    val ownerName: String = EMPTY_STRING,
    val ownerEmail: String = EMPTY_STRING,
    val ownerPhone: String = EMPTY_STRING,
    val ownerProfileImage: String = EMPTY_STRING,
    val cafeId: String = EMPTY_STRING,
    val cafeName: String = EMPTY_STRING,
    val cafeAddress: String = EMPTY_STRING,
    val cafePhone: String = EMPTY_STRING,
    val cafeOpenTime: String = EMPTY_STRING,
    val cafeCloseTime: String = EMPTY_STRING,
    val cafeImageUrl: String = EMPTY_STRING,
    val cafeRating: Float = 0.0f
)

data class CustomerModel(
    val id: String = EMPTY_STRING,
    val name: String = EMPTY_STRING,
    val email: String = EMPTY_STRING,
    val phone: String = EMPTY_STRING,
    val defaultAddress: String = EMPTY_STRING,
    val addresses: List<String> = emptyList(),
    val profileImageUrl: String = EMPTY_STRING
)

data class OrderItemModel(
    val foodId: Int = 0,
    val quantity: Int = 0,
    val price: Double = 0.0
)

data class OrderModel(
    val id: String = EMPTY_STRING,
    val customerId: String = EMPTY_STRING,
    val cafeId: String = EMPTY_STRING,
    val items: List<OrderItemModel> = emptyList(),
    val totalPrice: Double = 0.0,
    val status: OrderStatus = OrderStatus.ORDERED,
    val timestamp: Long = System.currentTimeMillis(),
    val deliveryAddress: String = EMPTY_STRING
)

data class CategoryModel(
    val id: String = "",
    val name: String = "",
    val icon: String = ""
)

data class CartItemModel(
    val foodId: Int = 0,
    val quantity: Int = 0
)

data class CartModel(
    val customerId: String = "",
    val items: List<CartItemModel> = emptyList()
)


enum class OrderStatus(val value: String) {
    ORDERED("ORDERED"),
    COOKING("COOKING"),
    DELIVERING("DELIVERING"),
    COMPLETED("COMPLETED");

    companion object {
        fun fromValue(value: String): OrderStatus {
            return OrderStatus.entries.find { it.value == value.uppercase() } ?: ORDERED
        }
    }
}
