/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: BottomNavigationBar.kt
 *
 * Last modified by Dedy Wijaya on 31/12/2025 11.08
 */

package com.mtv.app.shopme.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mtv.app.shopme.common.AppColor

@Composable
fun BottomNavigationBar(navController: NavController) {

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Cart,
        BottomNavItem.Search,
        BottomNavItem.Chat,
        BottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val activeColorBox = AppColor.Orange
    val activeColorIcon = AppColor.White
    val inactiveColorIcon = AppColor.Orange

    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        NavigationBar(
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .height(64.dp)
                .clip(RoundedCornerShape(32.dp))
                .border(1.dp, AppColor.Orange.copy(alpha = 0.2f), RoundedCornerShape(32.dp))
                .background(Color.White),
            containerColor = Color.White,
            tonalElevation = 0.dp
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Box(
                            modifier = if (isSelected) {
                                Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(activeColorBox)
                                    .padding(8.dp)
                            } else {
                                Modifier.padding(8.dp)
                            },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = if (isSelected) activeColorIcon else inactiveColorIcon,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    alwaysShowLabel = false,
                    label = null,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.White
                    )
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    device = Devices.PIXEL_4
)
@Composable
fun PreviewBottomNavigationBar() {
    val navController = rememberNavController()

    BottomNavigationBar(
        navController = navController
    )
}
