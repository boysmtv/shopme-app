/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppNavigation.kt
 *
 * Last modified by Dedy Wijaya on 31/12/2025 11.08
 */

package com.mtv.app.shopme.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomRoutes = listOf(
        BottomNavItem.Home.route,
        BottomNavItem.Cart.route,
        BottomNavItem.Search.route,
        BottomNavItem.Map.route,
        BottomNavItem.Profile.route
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomRoutes) {
                BottomNavigationBar(navController)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            AppNavGraph(navController)
        }
    }
}