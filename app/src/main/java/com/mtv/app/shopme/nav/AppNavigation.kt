/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppNavigation.kt
 *
 * Last modified by Dedy Wijaya on 31/12/2025 11.08
 */

package com.mtv.app.shopme.nav

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mtv.app.shopme.common.navbar.customer.CustomerBottomNavItem
import com.mtv.app.shopme.common.navbar.customer.CustomerBottomNavigationBar
import com.mtv.app.shopme.common.navbar.customer.CustomerDestinations
import com.mtv.app.shopme.common.navbar.seller.SellerBottomNavItem
import com.mtv.app.shopme.common.navbar.seller.SellerBottomNavigationBar
import com.mtv.app.shopme.common.navbar.seller.SellerDestinations
import com.mtv.app.shopme.common.ConstantPreferences.USER_ROLE
import com.mtv.app.shopme.common.notification.NotificationDeepLink
import com.mtv.app.shopme.common.navbar.auth.AuthDestinations
import com.mtv.based.core.provider.utils.SecurePrefs
import com.mtv.based.core.provider.utils.SessionManager

@Composable
fun AppNavigation(
    sessionManager: SessionManager,
    notificationDeepLink: NotificationDeepLink? = null,
    onNotificationDeepLinkConsumed: () -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current
    var showExitDialog by remember { mutableStateOf(false) }
    val securePrefs = remember(context) { SecurePrefs(context) }

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
        SellerBottomNavItem.Chat.route,
        SellerBottomNavItem.Order.route,
        SellerBottomNavItem.Profile.route
    )

    val isMainTab = currentRoute in bottomRoutes || currentRoute in sellerRoutes

    LaunchedEffect(notificationDeepLink, currentRoute) {
        val deepLink = notificationDeepLink ?: return@LaunchedEffect
        if (currentRoute == null || currentRoute == AuthDestinations.SPLASH_GRAPH || currentRoute == AuthDestinations.LOGIN_GRAPH) {
            return@LaunchedEffect
        }

        val savedRole = securePrefs.getString(USER_ROLE).orEmpty()
        val isSeller = if (savedRole.isNotBlank()) {
            savedRole.equals("SELLER", ignoreCase = true)
        } else {
            deepLink.role.equals("seller", ignoreCase = true)
        }
        val targetRoute = when {
            deepLink.isChat && isSeller -> SellerDestinations.navigateToChatDetail(deepLink.conversationId)
            deepLink.isChat -> CustomerDestinations.navigateToChat(deepLink.conversationId)
            deepLink.isOrder && isSeller -> SellerDestinations.navigateToOrderDetail(deepLink.orderId)
            deepLink.isOrder -> CustomerDestinations.navigateToOrderDetail(deepLink.orderId)
            else -> null
        }

        if (targetRoute != null && currentRoute != targetRoute) {
            navController.navigate(targetRoute) {
                launchSingleTop = true
            }
        }
        onNotificationDeepLinkConsumed()
    }

    BackHandler(enabled = isMainTab) {
        showExitDialog = true
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Anda ingin keluar?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        (context as? Activity)?.finish()
                    }
                ) {
                    Text("Keluar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

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
            AppNavGraph(
                navController = navController,
                sessionManager = sessionManager
            )
        }
    }
}
