/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NavGraphBuilder.kt
 *
 * Last modified by Dedy Wijaya on 31/12/2025 11.08
 */

package com.mtv.app.shopme.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mtv.app.shopme.feature.route.CartRoute
import com.mtv.app.shopme.feature.route.DetailRoute
import com.mtv.app.shopme.feature.route.HomeRoute
import com.mtv.app.shopme.feature.route.MapRoute
import com.mtv.app.shopme.feature.route.NotifRoute
import com.mtv.app.shopme.feature.route.ProfileRoute
import com.mtv.app.shopme.feature.route.SearchRoute
import com.mtv.app.shopme.feature.route.SplashRoute

fun NavGraphBuilder.splashGraph(nav: NavHostController) {
    composable(AppDestinations.SPLASH_GRAPH) {
        SplashRoute(nav)
    }
}

fun NavGraphBuilder.authGraph(nav: NavHostController) {
    composable(AppDestinations.LOGIN_GRAPH) {
//        LoginRoute(nav)
    }

    composable(AppDestinations.REGISTER_GRAPH) {
//        RegisterRoute(nav)
    }

    composable(AppDestinations.RESET_GRAPH) {
//        ResetRoute(nav)
    }
}

fun NavGraphBuilder.homeGraph(nav: NavHostController) {
    navigation(
        startDestination = BottomNavItem.Home.route,
        route = AppDestinations.HOME_GRAPH
    ) {
        composable(BottomNavItem.Home.route) {
            HomeRoute(nav)
        }
        composable(BottomNavItem.Cart.route) {
            CartRoute(nav)
        }
        composable(BottomNavItem.Search.route) {
            SearchRoute(nav)
        }
        composable(BottomNavItem.Map.route) {
            MapRoute(nav)
        }
        composable(BottomNavItem.Profile.route) {
            ProfileRoute(nav)
        }
    }
}

fun NavGraphBuilder.detailGraph(nav: NavHostController) {
    composable(AppDestinations.DETAIL_GRAPH) {
        DetailRoute(nav)
    }
}

fun NavGraphBuilder.notificationGraph(nav: NavHostController) {
    composable(AppDestinations.NOTIFICATION_GRAPH) {
        NotifRoute(nav)
    }
}