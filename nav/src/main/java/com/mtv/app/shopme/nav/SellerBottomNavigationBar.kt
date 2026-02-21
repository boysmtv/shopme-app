/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerBottomNavigationBar.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 12.32
 */

package com.mtv.app.shopme.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mtv.app.shopme.common.AppColor

@Composable
fun SellerBottomNavigationBar(navController: NavController) {

    val items = listOf(
        SellerBottomNavItem.Dashboard,
        SellerBottomNavItem.Order,
        SellerBottomNavItem.Chat,
        SellerBottomNavItem.Product,
        SellerBottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .background(AppColor.White)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        NavigationBar(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .height(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, AppColor.Blue.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                .background(AppColor.White),
            containerColor = AppColor.White,
            tonalElevation = 0.dp
        ) {
            items.forEach { item ->

                val isSelected = currentRoute == item.route

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) AppColor.Blue else Color.Transparent),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = if (isSelected) AppColor.White else AppColor.Blue,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            if (isSelected) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.title,
                                    color = AppColor.Blue,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    },
                    label = null,
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun PreviewSellerBottomNavigationBar() {
    val navController = rememberNavController()
    SellerBottomNavigationBar(navController = navController)
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun PreviewSellerBottomNavigationBarActive() {
    SellerBottomNavigationBarPreview(activeRoute = SellerBottomNavItem.Dashboard.route)
}

@Composable
fun SellerBottomNavigationBarPreview(activeRoute: String) {

    val items = listOf(
        SellerBottomNavItem.Dashboard,
        SellerBottomNavItem.Order,
        SellerBottomNavItem.Chat,
        SellerBottomNavItem.Product,
        SellerBottomNavItem.Profile
    )

    Box(
        modifier = Modifier
            .background(AppColor.White)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        NavigationBar(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .height(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, AppColor.Blue.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                .background(AppColor.White),
            containerColor = AppColor.White,
            tonalElevation = 0.dp
        ) {
            items.forEach { item ->

                val isSelected = activeRoute == item.route

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { /* kosong di preview */ },
                    icon = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) AppColor.Blue else Color.Transparent),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = if (isSelected) AppColor.White else AppColor.Blue,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            if (isSelected) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.title,
                                    color = AppColor.Blue,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    },
                    label = null,
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}
