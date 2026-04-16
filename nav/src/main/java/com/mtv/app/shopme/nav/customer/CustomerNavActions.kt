/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CustomerNavActions.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 14.17
 */

package com.mtv.app.shopme.nav.customer

import androidx.navigation.NavController
import com.mtv.app.shopme.common.navbar.auth.AuthDestinations
import com.mtv.app.shopme.common.navbar.customer.CustomerBottomNavItem
import com.mtv.app.shopme.common.navbar.customer.CustomerDestinations
import com.mtv.app.shopme.common.navbar.seller.SellerDestinations
import com.mtv.app.shopme.nav.extensions.navigateAndPopSplash

object CustomerNavActions {

    fun toLogin(nav: NavController) {
        nav.navigate(AuthDestinations.LOGIN_GRAPH) {
            popUpTo(0) { inclusive = true }
        }
    }

    fun toHome(nav: NavController) {
        nav.navigate(CustomerDestinations.HOME_GRAPH) {
            popUpTo(0) { inclusive = true }
        }
    }

    fun toRegister(nav: NavController) {
        nav.navigate(AuthDestinations.REGISTER_GRAPH)
    }

    fun toForgetPassword(nav: NavController) {
        nav.navigate(AuthDestinations.RESET_GRAPH)
    }

    fun toSearch(nav: NavController) {
        nav.navigate(CustomerBottomNavItem.Search.route) {
            launchSingleTop = true
            restoreState = true
            popUpTo(nav.graph.startDestinationId) {
                saveState = true
            }
        }
    }

    fun toCart(nav: NavController) {
        nav.navigate(CustomerBottomNavItem.Cart.route) {
            launchSingleTop = true
            restoreState = true
            popUpTo(nav.graph.startDestinationId) {
                saveState = true
            }
        }
    }

    fun toSeller(nav: NavController) {
        nav.navigate(SellerDestinations.SELLER_GRAPH) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun toNotif(nav: NavController) {
        nav.navigate(CustomerDestinations.NOTIF_GRAPH)
    }

    fun toOrder(nav: NavController) {
        nav.navigate(CustomerDestinations.ORDER_GRAPH)
    }

    fun toChat(nav: NavController) {
        nav.navigate(CustomerDestinations.CHAT_GRAPH)
    }

    fun toEditProfile(nav: NavController) {
        nav.navigate(CustomerDestinations.EDIT_PROFILE_GRAPH)
    }

    fun toOrderHistory(nav: NavController) {
        nav.navigate(CustomerDestinations.ORDER_HISTORY_GRAPH)
    }

    fun toSettings(nav: NavController) {
        nav.navigate(CustomerDestinations.SETTINGS_GRAPH)
    }

    fun toHelpCenter(nav: NavController) {
        nav.navigate(CustomerDestinations.HELP_GRAPH)
    }

    fun toTnc(nav: NavController) {
        nav.navigate(SellerDestinations.SELLER_CREATE_TNC_GRAPH)
    }

    fun toDetail(nav: NavController, id: String) {
        nav.navigate(CustomerDestinations.navigateToDetail(id))
    }

    fun toCafe(nav: NavController, id: String) {
        nav.navigate(CustomerDestinations.navigateToCafe(id))
    }
}