/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: BottomNavItem.kt
 *
 * Last modified by Dedy Wijaya on 31/12/2025 11.08
 */

package com.mtv.app.shopme.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class CustomerBottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Home : CustomerBottomNavItem(route = "home", icon = Icons.Filled.Home, label = "Home")
    object Cart : CustomerBottomNavItem(route = "shop", icon = Icons.Filled.ShoppingCart, label = "Shop")
    object Search : CustomerBottomNavItem(route = "search", icon = Icons.Filled.Search, label = "Search")
    object Chat : CustomerBottomNavItem(route = "chat", icon = Icons.AutoMirrored.Filled.Chat, label = "Chat")
    object Profile : CustomerBottomNavItem(route = "profile", icon = Icons.Filled.Person, label = "Profile")
}
