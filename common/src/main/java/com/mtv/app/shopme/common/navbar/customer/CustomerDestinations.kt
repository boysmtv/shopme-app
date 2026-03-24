package com.mtv.app.shopme.common.navbar.customer

object CustomerDestinations {

    const val HOME = "HOME"
    const val CART = "CART"
    const val SEARCH = "SEARCH"
    const val CHAT = "CHAT"
    const val PROFILE = "PROFILE"
    const val HOME_GRAPH = "HOME_GRAPH"
    const val RESET_GRAPH = "FORGOT_GRAPH"
    const val DETAIL_GRAPH = "DETAIL_GRAPH"
    const val DETAIL_GRAPH_WITH_ID = "DETAIL_GRAPH_WITH_ID/{foodId}"
    const val PROFILE_GRAPH = "PROFILE_GRAPH"
    const val EDIT_PROFILE_GRAPH = "EDIT_PROFILE_GRAPH"
    const val NOTIF_GRAPH = "NOTIF_GRAPH"
    const val CHAT_GRAPH = "CHAT_GRAPH"
    const val ORDER_GRAPH = "ORDER_GRAPH"
    const val CAFE_GRAPH = "CAFE_GRAPH"
    const val CAFE_GRAPH_WITH_ID = "CAFE_GRAPH_WITH_ID/{cafeId}"
    const val ORDER_DETAIL_GRAPH = "ORDER_DETAIL_GRAPH"
    const val ORDER_HISTORY_GRAPH = "ORDER_HISTORY_GRAPH"
    const val SETTINGS_GRAPH = "SETTINGS_GRAPH"
    const val SUPPORT_GRAPH = "SUPPORT_GRAPH"
    const val HELP_GRAPH = "HELP_GRAPH"
    const val CHAT_SUPPORT_GRAPH = "CHAT_SUPPORT_GRAPH"
    const val NOTIFICATION_GRAPH = "NOTIFICATION_GRAPH"
    const val SECURITY_GRAPH = "SECURITY_GRAPH"

    fun navigateToDetail(foodId: String) = "DETAIL_GRAPH_WITH_ID/$foodId"

    fun navigateToCafe(cafeId: String) = "CAFE_GRAPH_WITH_ID/$cafeId"

}