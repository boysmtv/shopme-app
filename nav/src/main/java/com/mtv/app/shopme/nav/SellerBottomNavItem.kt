/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerBottomNavItem.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 12.32
 */

package com.mtv.app.shopme.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Store
import androidx.compose.ui.graphics.vector.ImageVector

sealed class SellerBottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Dashboard : SellerBottomNavItem(
        route = SellerDestinations.DASHBOARD,
        title = "Dashboard",
        icon = Icons.Default.Home
    )

    object Product : SellerBottomNavItem(
        route = SellerDestinations.PRODUCT,
        title = "Products",
        icon = Icons.Default.Store
    )

    object Order : SellerBottomNavItem(
        route = SellerDestinations.ORDER,
        title = "Orders",
        icon = Icons.Default.Receipt
    )

    object Profile : SellerBottomNavItem(
        route = SellerDestinations.PROFILE,
        title = "Profile",
        icon = Icons.Default.Person
    )
}
