/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerBottomNavigationBar.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 12.32
 */

package com.mtv.app.shopme.nav

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun SellerBottomNavigationBar(navController: NavController) {

    val items = listOf(
        SellerBottomNavItem.Dashboard,
        SellerBottomNavItem.Product,
        SellerBottomNavItem.Order,
        SellerBottomNavItem.Profile
    )

    NavigationBar {

        val currentRoute =
            navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { item ->

            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) }
            )
        }
    }
}
