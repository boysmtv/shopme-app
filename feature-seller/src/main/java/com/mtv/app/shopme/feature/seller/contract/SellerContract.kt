/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerContract.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 12.16
 */

package com.mtv.app.shopme.feature.seller.contract

import com.mtv.based.core.network.utils.ResourceFirebase

data class SellerStateListener(
    val emptyState: ResourceFirebase<Unit> = ResourceFirebase.Loading
)

data class SellerDataListener(
    val emptyData: String? = null
)

data class SellerEventListener(
    val onRefresh: () -> Unit = {}
)

data class SellerNavigationListener(
    val onNavigateToProduct: () -> Unit = {},
    val onNavigateToOrder: () -> Unit = {},
    val onNavigateToOrderDetail: (String) -> Unit = {}
)
