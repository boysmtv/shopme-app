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
import com.mtv.app.shopme.feature.auth.route.LoginRoute
import com.mtv.app.shopme.feature.auth.route.RegisterRoute
import com.mtv.app.shopme.feature.auth.route.ResetRoute
import com.mtv.app.shopme.feature.auth.route.SplashRoute
import com.mtv.app.shopme.feature.customer.route.CafeRoute
import com.mtv.app.shopme.feature.customer.route.CartRoute
import com.mtv.app.shopme.feature.customer.route.ChatRoute
import com.mtv.app.shopme.feature.customer.route.DetailRoute
import com.mtv.app.shopme.feature.customer.route.EditAddressRoute
import com.mtv.app.shopme.feature.customer.route.EditProfileRoute
import com.mtv.app.shopme.feature.customer.route.HomeRoute
import com.mtv.app.shopme.feature.customer.route.ChatListRoute
import com.mtv.app.shopme.feature.customer.route.NotifRoute
import com.mtv.app.shopme.feature.customer.route.OrderRoute
import com.mtv.app.shopme.feature.customer.route.ProfileRoute
import com.mtv.app.shopme.feature.customer.route.SearchRoute
import com.mtv.app.shopme.feature.seller.route.SellerChatDetailRoute
import com.mtv.app.shopme.feature.seller.route.SellerChatListRoute
import com.mtv.app.shopme.feature.seller.route.SellerDashboardRoute
import com.mtv.app.shopme.feature.seller.route.SellerNotifRoute
import com.mtv.app.shopme.feature.seller.route.SellerOrderDetailRoute
import com.mtv.app.shopme.feature.seller.route.SellerOrderRoute
import com.mtv.app.shopme.feature.seller.route.SellerProductFormRoute
import com.mtv.app.shopme.feature.seller.route.SellerProductListRoute
import com.mtv.app.shopme.feature.seller.route.SellerProfileRoute

fun NavGraphBuilder.authGraph(nav: NavHostController) {
    composable(AuthDestinations.SPLASH_GRAPH) {
        SplashRoute(nav)
    }
    composable(AuthDestinations.LOGIN_GRAPH) {
        LoginRoute(nav)
    }

    composable(AuthDestinations.REGISTER_GRAPH) {
        RegisterRoute(nav)
    }

    composable(AuthDestinations.RESET_GRAPH) {
        ResetRoute(nav)
    }
}

fun NavGraphBuilder.homeGraph(nav: NavHostController) {
    navigation(
        startDestination = CustomerBottomNavItem.Home.route,
        route = CustomerDestinations.HOME_GRAPH
    ) {
        composable(CustomerBottomNavItem.Home.route) {
            HomeRoute(nav)
        }
        composable(CustomerBottomNavItem.Cart.route) {
            CartRoute(nav)
        }
        composable(CustomerBottomNavItem.Search.route) {
            SearchRoute(nav)
        }
        composable(CustomerBottomNavItem.Chat.route) {
            ChatListRoute(nav)
        }
        composable(CustomerBottomNavItem.Profile.route) {
            ProfileRoute(nav)
        }
    }
}

fun NavGraphBuilder.detailGraph(nav: NavHostController) {
    composable(CustomerDestinations.DETAIL_GRAPH) {
        DetailRoute(nav)
    }
}

fun NavGraphBuilder.notificationGraph(nav: NavHostController) {
    composable(CustomerDestinations.NOTIF_GRAPH) {
        NotifRoute(nav)
    }
    composable(CustomerDestinations.CHAT_GRAPH) {
        ChatRoute(nav)
    }
}

fun NavGraphBuilder.profileGraph(nav: NavHostController) {
    composable(CustomerDestinations.PROFILE_GRAPH) {
        ProfileRoute(nav)
    }
    composable(CustomerDestinations.EDIT_PROFILE_GRAPH) {
        EditProfileRoute(nav)
    }
    composable(CustomerDestinations.EDIT_ADDRESS_GRAPH) {
        EditAddressRoute(nav)
    }
}

fun NavGraphBuilder.orderGraph(nav: NavHostController) {
    composable(CustomerDestinations.CAFE_GRAPH) {
        CafeRoute(nav)
    }
    composable(CustomerDestinations.ORDER_GRAPH) {
        OrderRoute(nav)
    }
}

fun NavGraphBuilder.sellerGraph(nav: NavHostController) {
    navigation(
        startDestination = SellerDestinations.DASHBOARD,
        route = SellerDestinations.SELLER_GRAPH
    ) {
        composable(SellerDestinations.DASHBOARD) {
            SellerDashboardRoute(nav)
        }

        composable(SellerDestinations.ORDER) {
            SellerOrderRoute(nav)
        }

        composable(SellerDestinations.CHAT) {
            SellerChatListRoute(nav)
        }

        composable(SellerDestinations.PRODUCT) {
             SellerProductListRoute(nav)
        }

        composable(SellerDestinations.PROFILE) {
             SellerProfileRoute(nav)
        }
    }
}

fun NavGraphBuilder.sellerDetailGraph(nav: NavHostController) {
    composable(SellerDestinations.SELLER_ORDER_DETAIL_GRAPH) {
        SellerOrderDetailRoute(nav)
    }

    composable(SellerDestinations.SELLER_CHAT_DETAIL_GRAPH) {
        SellerChatDetailRoute(nav)
    }

    composable(SellerDestinations.SELLER_PRODUCT_ADD_GRAPH) {
        SellerProductFormRoute(nav)
    }

    composable(SellerDestinations.SELLER_NOTIFICATION_GRAPH) {
        SellerNotifRoute(nav)
    }
}

