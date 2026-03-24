/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CustomerNavActions.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 14.17
 */

package com.mtv.app.shopme.nav.customer

import androidx.navigation.NavController
import com.mtv.app.shopme.common.navbar.customer.CustomerBottomNavItem
import com.mtv.app.shopme.common.navbar.customer.CustomerDestinations

object CustomerNavActions {

    fun toSearch(nav: NavController) {
        nav.navigate(CustomerBottomNavItem.Search.route) {
            launchSingleTop = true
            restoreState = true
            popUpTo(nav.graph.startDestinationId) {
                saveState = true
            }
        }
    }

    fun toDetail(nav: NavController, id: String) {
        nav.navigate(CustomerDestinations.navigateToDetail(id))
    }

    fun toNotif(nav: NavController) {
        nav.navigate(CustomerDestinations.NOTIF_GRAPH)
    }
}