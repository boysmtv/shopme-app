/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: BottomNavItem.kt
 *
 * Last modified by Dedy Wijaya on 31/12/2025 11.08
 */

package com.mtv.app.shopme.common.navbar.customer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class CustomerBottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
) {
    object Home : CustomerBottomNavItem(
        route = CustomerDestinations.HOME,
        icon = Icons.Filled.Home,
        title = "Home"
    )

    object Cart : CustomerBottomNavItem(
        route = CustomerDestinations.CART,
        icon = Icons.Filled.ShoppingCart,
        title = "Shop"
    )

    object Search : CustomerBottomNavItem(
        route = CustomerDestinations.SEARCH,
        icon = Icons.Filled.Search,
        title = "Search"
    )

    object Chat : CustomerBottomNavItem(
        route = CustomerDestinations.CHAT,
        icon = Icons.AutoMirrored.Filled.Chat,
        title = "Chat"
    )

    object Profile : CustomerBottomNavItem(
        route = CustomerDestinations.PROFILE,
        icon = Icons.Filled.Person,
        title = "Profile"
    )
}
