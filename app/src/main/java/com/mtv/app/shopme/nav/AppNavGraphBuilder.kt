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
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.mtv.app.shopme.nav.route.auth.ChangePinRoute
import com.mtv.app.shopme.nav.route.auth.LoginRoute
import com.mtv.app.shopme.nav.route.auth.PasswordRoute
import com.mtv.app.shopme.nav.route.auth.RegisterRoute
import com.mtv.app.shopme.nav.route.auth.ResetRoute
import com.mtv.app.shopme.nav.route.auth.SplashRoute
import com.mtv.app.shopme.nav.route.customer.SettingsRoute
import com.mtv.app.shopme.nav.route.customer.CafeRoute
import com.mtv.app.shopme.nav.route.customer.CartRoute
import com.mtv.app.shopme.nav.route.customer.ChatRoute
import com.mtv.app.shopme.nav.route.customer.DetailRoute
import com.mtv.app.shopme.nav.route.customer.EditProfileRoute
import com.mtv.app.shopme.nav.route.customer.HomeRoute
import com.mtv.app.shopme.nav.route.customer.ChatListRoute
import com.mtv.app.shopme.nav.route.customer.ChatSupportRoute
import com.mtv.app.shopme.nav.route.customer.HelpRoute
import com.mtv.app.shopme.nav.route.customer.NotifRoute
import com.mtv.app.shopme.nav.route.customer.NotificationRoute
import com.mtv.app.shopme.nav.route.customer.OrderDetailRoute
import com.mtv.app.shopme.nav.route.customer.OrderHistoryRoute
import com.mtv.app.shopme.nav.route.customer.OrderRoute
import com.mtv.app.shopme.nav.route.customer.ProfileRoute
import com.mtv.app.shopme.nav.route.customer.SearchRoute
import com.mtv.app.shopme.nav.route.customer.SecurityRoute
import com.mtv.app.shopme.nav.route.customer.SupportRoute
import com.mtv.app.shopme.nav.route.seller.SellerChatDetailRoute
import com.mtv.app.shopme.nav.route.seller.SellerChatListRoute
import com.mtv.app.shopme.nav.route.seller.SellerCreateCafeRoute
import com.mtv.app.shopme.nav.route.seller.SellerCreateCafeTncRoute
import com.mtv.app.shopme.nav.route.seller.SellerDashboardRoute
import com.mtv.app.shopme.nav.route.seller.SellerEditStoreRoute
import com.mtv.app.shopme.nav.route.seller.SellerNotifRoute
import com.mtv.app.shopme.nav.route.seller.SellerOrderDetailRoute
import com.mtv.app.shopme.nav.route.seller.SellerOrderRoute
import com.mtv.app.shopme.nav.route.seller.SellerPaymentMethodRoute
import com.mtv.app.shopme.nav.route.seller.SellerProductFormRoute
import com.mtv.app.shopme.nav.route.seller.SellerProductListRoute
import com.mtv.app.shopme.nav.route.seller.SellerStoreRoute
import com.mtv.app.shopme.common.navbar.auth.AuthDestinations
import com.mtv.app.shopme.common.navbar.customer.CustomerBottomNavItem
import com.mtv.app.shopme.common.navbar.customer.CustomerDestinations
import com.mtv.app.shopme.common.navbar.seller.SellerDestinations

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

    composable(AuthDestinations.PASSWORD_GRAPH) {
        PasswordRoute(nav)
    }

    composable(AuthDestinations.CHANGE_PIN_GRAPH) {
        ChangePinRoute(nav)
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
        composable(
            route = CustomerDestinations.SEARCH_WITH_QUERY,
            arguments = listOf(navArgument("query") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
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

    composable(
        route = CustomerDestinations.DETAIL_GRAPH_WITH_ID,
        arguments = listOf(
            navArgument("foodId") {
                type = NavType.StringType
            }
        )
    ) {
        DetailRoute(nav = nav)
    }
}

fun NavGraphBuilder.notificationGraph(nav: NavHostController) {
    composable(CustomerDestinations.NOTIF_GRAPH) {
        NotifRoute(nav)
    }
    composable(CustomerDestinations.CHAT_GRAPH) {
        ChatRoute(nav)
    }
    composable(
        route = CustomerDestinations.CHAT_GRAPH_WITH_ID,
        arguments = listOf(
            navArgument("chatId") {
                type = NavType.StringType
            }
        )
    ) {
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
    composable(CustomerDestinations.ORDER_HISTORY_GRAPH) {
        OrderHistoryRoute(nav)
    }
    composable(CustomerDestinations.SETTINGS_GRAPH) {
        SettingsRoute(nav)
    }
    composable(CustomerDestinations.SUPPORT_GRAPH) {
        SupportRoute(nav)
    }
    composable(CustomerDestinations.CHAT_SUPPORT_GRAPH) {
        ChatSupportRoute(nav)
    }
    composable(CustomerDestinations.NOTIFICATION_GRAPH) {
        NotificationRoute(nav)
    }
    composable(CustomerDestinations.SECURITY_GRAPH) {
        SecurityRoute(nav)
    }
    composable(CustomerDestinations.HELP_GRAPH) {
        HelpRoute(nav)
    }
}

fun NavGraphBuilder.orderGraph(nav: NavHostController) {
    composable(CustomerDestinations.CAFE_GRAPH) {
        CafeRoute(nav)
    }

    composable(
        route = CustomerDestinations.CAFE_GRAPH_WITH_ID,
        arguments = listOf(
            navArgument("cafeId") {
                type = NavType.StringType
            }
        )
    ) {
        CafeRoute(nav = nav)
    }

    composable(CustomerDestinations.ORDER_GRAPH) {
        OrderRoute(nav)
    }
    composable(
        route = CustomerDestinations.ORDER_DETAIL_GRAPH_WITH_ID,
        arguments = listOf(navArgument("orderId") { type = NavType.StringType })
    ) {
        OrderDetailRoute(nav)
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
             SellerStoreRoute(nav)
        }
    }
}

fun NavGraphBuilder.sellerDetailGraph(nav: NavHostController) {
    composable(SellerDestinations.SELLER_CREATE_TNC_GRAPH) {
        SellerCreateCafeTncRoute(nav)
    }
    composable(SellerDestinations.SELLER_CREATE_GRAPH) {
        SellerCreateCafeRoute(nav)
    }

    composable(
        route = SellerDestinations.SELLER_ORDER_DETAIL_GRAPH,
        arguments = listOf(navArgument("orderId") { type = NavType.StringType })
    ) {
        SellerOrderDetailRoute(nav)
    }

    composable(
        route = SellerDestinations.SELLER_CHAT_DETAIL_GRAPH,
        arguments = listOf(navArgument("chatId") { type = NavType.StringType })
    ) {
        SellerChatDetailRoute(nav)
    }

    composable(SellerDestinations.SELLER_PRODUCT_ADD_GRAPH) {
        SellerProductFormRoute(nav)
    }
    composable(
        route = SellerDestinations.SELLER_PRODUCT_EDIT_GRAPH,
        arguments = listOf(navArgument("productId") { type = NavType.StringType })
    ) {
        SellerProductFormRoute(nav)
    }

    composable(SellerDestinations.SELLER_NOTIFICATION_GRAPH) {
        SellerNotifRoute(nav)
    }
    composable(SellerDestinations.SELLER_EDIT_STORE_GRAPH) {
        SellerEditStoreRoute(nav)
    }
    composable(SellerDestinations.SELLER_PAYMENT_METHOD_GRAPH) {
        SellerPaymentMethodRoute(nav)
    }
}
