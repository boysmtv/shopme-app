/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: BottomNavigationBar.kt
 *
 * Last modified by Dedy Wijaya on 31/12/2025 11.08
 */

package com.mtv.app.shopme.nav

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

@Composable
fun BottomNavigationBar(navController: NavController) {

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Cart,
        BottomNavItem.Search,
        BottomNavItem.Map,
        BottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val inactiveColor = Color(0xFFE0E0E0) // LightGray
    val activeColor = Color(0xFFFF8A00)   // Orange
    val textColor = Color(0xFF424242)     // DarkGray

    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.height(72.dp)
    ) {
        items.forEachIndexed { index, item ->

            val isSelected = currentRoute == item.route
            val isFirst = index == 0
            val isLast = index == items.lastIndex

            val chipWidth by animateDpAsState(
                targetValue = 90.dp,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = CubicBezierEasing(0.25f, 0.1f, 0.25f, 1f)
                )
            )

            val chipColor by animateColorAsState(
                targetValue = if (isSelected) activeColor else inactiveColor,
                animationSpec = tween(300)
            )

            val iconTint by animateColorAsState(
                targetValue = if (isSelected) Color.White else textColor,
                animationSpec = tween(250)
            )

            NavigationBarItem(
                selected = isSelected,
                onClick = {}, // klik dipindah ke chip
                icon = {
                    Row(
                        modifier = Modifier
                            .height(36.dp)
                            .width(chipWidth)
                            .clip(CircleShape)
                            .background(chipColor)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null // ⛔ ripple default dimatikan
                            ) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = iconTint,
                            modifier = Modifier.size(22.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = item.label,
                            color = textColor,
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                ),
                modifier = Modifier.padding(
                    start = if (isFirst) 10.dp else 0.dp,
                    end = if (isLast) 10.dp else 0.dp
                )
            )
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
