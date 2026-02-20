/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NavController.kt
 *
 * Last modified by Dedy Wijaya on 31/12/2025 11.08
 */

package com.mtv.app.shopme.nav

import androidx.navigation.NavController

fun NavController.navigateAndPopSplash(route: String) {
    this.navigate(route) {
        popUpTo(AuthDestinations.SPLASH_GRAPH) { inclusive = true }
    }
}
