/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppNavigation.kt
 *
 * Last modified by Dedy Wijaya on 31/12/2025 11.08
 */

package com.mtv.app.shopme.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomRoutes = listOf(
        CustomerBottomNavItem.Home.route,
        CustomerBottomNavItem.Cart.route,
        CustomerBottomNavItem.Search.route,
        CustomerBottomNavItem.Chat.route,
        CustomerBottomNavItem.Profile.route
    )

    val sellerRoutes = listOf(
        SellerBottomNavItem.Dashboard.route,
        SellerBottomNavItem.Product.route,
        SellerBottomNavItem.Order.route,
        SellerBottomNavItem.Profile.route
    )

    Scaffold(
        bottomBar = {
            when (currentRoute) {
                in bottomRoutes -> CustomerBottomNavigationBar(navController)
                in sellerRoutes -> SellerBottomNavigationBar(navController)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(bottom = padding.calculateBottomPadding())
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            AppNavGraph(navController)
        }
    }
}