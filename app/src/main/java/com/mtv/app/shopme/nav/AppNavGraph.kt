/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppNavGraph.kt
 *
 * Last modified by Dedy Wijaya on 31/12/2025 11.08
 */

package com.mtv.app.shopme.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mtv.based.core.provider.utils.SessionManager

@Composable
fun AppNavGraph(
    navController: NavHostController,
    sessionManager: SessionManager
) {

    LaunchedEffect(Unit) {
        sessionManager.logoutEvent.collect {
            navController.navigate(AuthDestinations.LOGIN_GRAPH) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = AuthDestinations.SPLASH_GRAPH
    ) {
        authGraph(navController)
        homeGraph(navController)
        detailGraph(navController)
        notificationGraph(navController)
        profileGraph(navController)
        orderGraph(navController)
        sellerGraph(navController)
        sellerDetailGraph(navController)
    }
}
