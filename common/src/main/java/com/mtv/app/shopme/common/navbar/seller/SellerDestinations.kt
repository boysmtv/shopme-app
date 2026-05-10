/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerDestinations.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 12.14
 */

package com.mtv.app.shopme.common.navbar.seller

object SellerDestinations {
    const val SELLER_GRAPH = "SELLER_GRAPH"
    const val DASHBOARD = "SELLER_DASHBOARD"
    const val ORDER = "SELLER_ORDER"
    const val PRODUCT = "SELLER_PRODUCT"
    const val CHAT = "SELLER_CHAT"
    const val PROFILE = "SELLER_PROFILE"

    const val SELLER_CREATE_TNC_GRAPH = "SELLER_CREATE_TNC_GRAPH"
    const val SELLER_CREATE_GRAPH = "SELLER_CREATE_GRAPH"
    const val SELLER_ORDER_DETAIL_GRAPH = "SELLER_ORDER_DETAIL_GRAPH/{orderId}"
    const val SELLER_CHAT_DETAIL_GRAPH = "SELLER_CHAT_DETAIL_GRAPH/{chatId}"
    const val SELLER_PRODUCT_ADD_GRAPH = "SELLER_PRODUCT_ADD_GRAPH"
    const val SELLER_PRODUCT_EDIT_GRAPH = "SELLER_PRODUCT_EDIT_GRAPH/{productId}"
    const val SELLER_NOTIFICATION_GRAPH = "SELLER_NOTIFICATION_GRAPH"
    const val SELLER_EDIT_STORE_GRAPH = "SELLER_EDIT_STORE_GRAPH"
    const val SELLER_PAYMENT_METHOD_GRAPH = "SELLER_PAYMENT_METHOD_GRAPH"

    fun navigateToOrderDetail(orderId: String) = "SELLER_ORDER_DETAIL_GRAPH/$orderId"
    fun navigateToChatDetail(chatId: String) = "SELLER_CHAT_DETAIL_GRAPH/$chatId"
    fun navigateToProductEdit(productId: String) = "SELLER_PRODUCT_EDIT_GRAPH/$productId"
}
