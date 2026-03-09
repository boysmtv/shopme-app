/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.ChatResponse
import com.mtv.based.core.network.utils.Resource

data class ChatStateListener(
    val chatState: Resource<ApiResponse<ChatResponse>> = Resource.Loading,
    val chatSendMessageState: Resource<ApiResponse<Unit>> = Resource.Loading,
    val chatReadAllMessageState: Resource<ApiResponse<Unit>> = Resource.Loading,
)

data class ChatDataListener(
    val emptyData: String? = null
)

data class ChatEventListener(
    val onSendMessage: (String, String) -> Unit = { _, _ -> },
    val onReadAllMessage: (String, String) -> Unit = { _, _ -> },
)

data class ChatNavigationListener(
    val onBack: () -> Unit = {}
)