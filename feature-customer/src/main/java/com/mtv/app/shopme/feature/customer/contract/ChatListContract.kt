/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatListContract.kt
 *
 * Last modified by Dedy Wijaya on 13/02/26 13.33
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.ChatListResponse
import com.mtv.based.core.network.utils.Resource

data class ChatListStateListener(
    val chatListState: Resource<ApiResponse<ChatListResponse>> = Resource.Loading,
)

data class ChatListDataListener(
    val emptyData: String? = null
)

data class ChatListEventListener(
    val onClickItem: (String) -> Unit = {}
)

data class ChatListNavigationListener(
    val onBack: () -> Unit = {},
    val navigateToChat: (String) -> Unit = {}
)
