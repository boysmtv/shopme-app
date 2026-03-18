/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerOrderDetailContract.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 13.04
 */

package com.mtv.app.shopme.feature.seller.contract

import com.mtv.app.shopme.data.dto.OrderStatus
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

data class SellerOrderDetailStateListener(
    val orderId: String = EMPTY_STRING,
    val currentStatus: OrderStatus = OrderStatus.ORDERED
)

data class SellerOrderDetailDataListener(
    val customerName: String = EMPTY_STRING,
    val total: String = EMPTY_STRING
)

data class SellerOrderDetailEventListener(
    val onChangeStatus: (OrderStatus) -> Unit = {},
    val onSaveStatus: () -> Unit = {},
    val onDismissActiveDialog: () -> Unit = {}
)

data class SellerOrderDetailNavigationListener(
    val onBack: () -> Unit = {}
)
