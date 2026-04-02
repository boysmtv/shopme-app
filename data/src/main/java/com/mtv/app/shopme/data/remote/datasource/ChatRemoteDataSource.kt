/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: v.kt
 *
 * Last modified by Dedy Wijaya on 30/03/26 16.51
 */

package com.mtv.app.shopme.data.remote.datasource

import com.mtv.app.shopme.data.base.BaseRemoteDataSource
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.request.ChatMessageMarkAsReadRequest
import com.mtv.app.shopme.data.remote.request.ChatMessageSendRequest
import com.mtv.app.shopme.data.remote.response.ChatListResponse
import com.mtv.app.shopme.data.utils.requireData
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject

class ChatRemoteDataSource @Inject constructor(
    network: NetworkRepository
) : BaseRemoteDataSource(network) {

    suspend fun getChatList() =
        request<ApiResponse<ChatListResponse>>(
            endpoint = ApiEndPoint.Chat.GetList
        ).requireData()


    suspend fun getChats() =
        request<ApiResponse<ChatListResponse>>(
            endpoint = ApiEndPoint.Chat.Get
        ).requireData()

    suspend fun sendMessage(body: ChatMessageSendRequest) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Chat.SendMessage,
            body = body
        ).requireData()

    suspend fun readAll(body: ChatMessageMarkAsReadRequest) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Chat.MarkAllRead,
            body = body
        ).requireData()
}