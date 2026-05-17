package com.mtv.app.shopme.common.navbar.customer

object CustomerDestinations {

    const val HOME = "HOME"
    const val CART = "CART"
    const val SEARCH = "SEARCH"
    const val CHAT = "CHAT"
    const val PROFILE = "PROFILE"
    const val HOME_GRAPH = "HOME_GRAPH"
    const val RESET_GRAPH = "FORGOT_GRAPH"
    const val SEARCH_WITH_QUERY = "SEARCH/{query}"
    const val DETAIL_GRAPH = "DETAIL_GRAPH"
    const val DETAIL_GRAPH_WITH_ID = "DETAIL_GRAPH_WITH_ID/{foodId}"
    const val PROFILE_GRAPH = "PROFILE_GRAPH"
    const val EDIT_PROFILE_GRAPH = "EDIT_PROFILE_GRAPH"
    const val NOTIF_GRAPH = "NOTIF_GRAPH"
    const val CHAT_GRAPH = "CHAT_GRAPH"
    const val CHAT_GRAPH_WITH_ID = "CHAT_GRAPH/{chatId}"
    const val ORDER_GRAPH = "ORDER_GRAPH"
    const val ORDER_GRAPH_WITH_FILTER = "ORDER_GRAPH/{filter}"
    const val CAFE_GRAPH = "CAFE_GRAPH"
    const val CAFE_GRAPH_WITH_ID = "CAFE_GRAPH_WITH_ID/{cafeId}"
    const val ORDER_DETAIL_GRAPH = "ORDER_DETAIL_GRAPH"
    const val ORDER_DETAIL_GRAPH_WITH_ID = "ORDER_DETAIL_GRAPH/{orderId}"
    const val ORDER_HISTORY_GRAPH = "ORDER_HISTORY_GRAPH"
    const val FAVORITES_GRAPH = "FAVORITES_GRAPH"
    const val SETTINGS_GRAPH = "SETTINGS_GRAPH"
    const val SUPPORT_GRAPH = "SUPPORT_GRAPH"
    const val HELP_GRAPH = "HELP_GRAPH"
    const val CHAT_SUPPORT_GRAPH = "CHAT_SUPPORT_GRAPH"
    const val NOTIFICATION_GRAPH = "NOTIFICATION_GRAPH"
    const val SECURITY_GRAPH = "SECURITY_GRAPH"

    fun navigateToDetail(foodId: String) = "DETAIL_GRAPH_WITH_ID/$foodId"

    fun navigateToCafe(cafeId: String) = "CAFE_GRAPH_WITH_ID/$cafeId"

    fun navigateToChat(chatId: String) = "CHAT_GRAPH/$chatId"

    fun navigateToOrderDetail(orderId: String) = "ORDER_DETAIL_GRAPH/$orderId"

    fun navigateToOrder(filter: String) = "ORDER_GRAPH/$filter"

    fun navigateToSearch(query: String) = "SEARCH/$query"

}
