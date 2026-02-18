/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerRootScreen.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 12.35
 */

package com.mtv.app.shopme.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mtv.app.shopme.feature.seller.route.SellerOrderDetailRoute

@Composable
fun SellerRootScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            SellerBottomNavigationBar(navController)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            NavHost(
                navController = navController,
                startDestination = SellerDestinations.DASHBOARD
            ) {

                composable(SellerDestinations.DASHBOARD) {
//                    SellerDashboardRoute(navController)
                }

                composable(SellerDestinations.PRODUCT) {
//                    SellerProductRoute(navController)
                }

                composable(SellerDestinations.ORDER) {
//                    SellerOrderRoute(navController)
                }

                composable(SellerDestinations.PROFILE) {
//                    SellerProfileRoute(navController)
                }

                composable(
                    route = SellerDestinations.ORDER_DETAIL,
                    arguments = listOf(
                        navArgument("orderId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                    SellerOrderDetailRoute(
                        nav = navController,
                        orderId = orderId
                    )
                }

            }
        }
    }
}
