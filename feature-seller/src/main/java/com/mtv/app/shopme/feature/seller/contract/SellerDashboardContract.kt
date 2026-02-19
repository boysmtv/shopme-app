/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerDashboardContract.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 12.16
 */

package com.mtv.app.shopme.feature.seller.contract

import com.mtv.based.core.network.utils.ResourceFirebase

data class SellerDashboardStateListener(
    val emptyState: ResourceFirebase<Unit> = ResourceFirebase.Loading
)

data class SellerDashboardDataListener(
    val emptyData: String? = null
)

data class SellerDashboardEventListener(
    val onRefresh: () -> Unit = {}
)

data class SellerDashboardNavigationListener(
    val onNavigateToProduct: () -> Unit = {},
    val onNavigateToOrder: () -> Unit = {},
    val onNavigateToOrderDetail: (String) -> Unit = {}
)
