/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppNavGraph.kt
 *
 * Last modified by Dedy Wijaya on 31/12/2025 11.08
 */

package com.mtv.app.shopme.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.SPLASH_GRAPH
    ) {
        splashGraph(navController)
        authGraph(navController)
        homeGraph(navController)
        detailGraph(navController)
        notificationGraph(navController)
        profileGraph(navController)
    }
}
