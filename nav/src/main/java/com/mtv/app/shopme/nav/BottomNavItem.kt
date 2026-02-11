/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: BottomNavItem.kt
 *
 * Last modified by Dedy Wijaya on 31/12/2025 11.08
 */

package com.mtv.app.shopme.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Home : BottomNavItem(route = "home", icon = Icons.Filled.Home, label = "Home")
    object Cart : BottomNavItem(route = "shop", icon = Icons.Filled.ShoppingCart, label = "Shop")
    object Search : BottomNavItem(route = "search", icon = Icons.AutoMirrored.Filled.ManageSearch, label = "Search")
    object Map : BottomNavItem(route = "map", icon = Icons.Filled.Map, label = "Map")
    object Profile : BottomNavItem(route = "profile", icon = Icons.Filled.Person, label = "Profile")
}
