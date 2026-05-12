/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerDashboardContract.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 12.16
 */

package com.mtv.app.shopme.feature.seller.contract

import com.mtv.app.shopme.domain.model.SellerOrderItem
import com.mtv.based.core.network.utils.ResourceFirebase

data class SellerDashboardUiState(
    val isLoading: Boolean = false,
    val emptyState: ResourceFirebase<Unit> = ResourceFirebase.Loading,
    val errorMessage: String? = null,

    val orders: List<SellerOrderItem> = emptyList(),

    val storeName: String = "",
    val storeAddress: String = "",
    val notificationCount: Int = 0,
    val isOnline: Boolean = true,
    val selectedFilter: String = "All",
    val selectedSort: String = "Asc"
)

sealed class SellerDashboardEvent {
    object Load : SellerDashboardEvent()
    object DismissDialog : SellerDashboardEvent()

    object Refresh : SellerDashboardEvent()

    object ClickProduct : SellerDashboardEvent()
    object ClickOrder : SellerDashboardEvent()
    data class ClickOrderDetail(val orderId: String) : SellerDashboardEvent()
    object ClickNotif : SellerDashboardEvent()
    object ToggleOnline : SellerDashboardEvent()
    data class ChangeFilter(val value: String) : SellerDashboardEvent()
    data class ChangeSort(val value: String) : SellerDashboardEvent()
}

sealed class SellerDashboardEffect {
    object NavigateToProduct : SellerDashboardEffect()
    object NavigateToOrder : SellerDashboardEffect()
    data class NavigateToOrderDetail(val orderId: String) : SellerDashboardEffect()
    object NavigateToNotif : SellerDashboardEffect()
}
