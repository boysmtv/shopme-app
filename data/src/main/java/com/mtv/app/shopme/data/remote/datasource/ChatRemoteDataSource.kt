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
import com.mtv.app.shopme.data.remote.response.ChatConversationResponse
import com.mtv.app.shopme.data.remote.response.ChatResponse
import com.mtv.app.shopme.data.remote.response.ChatListResponse
import com.mtv.app.shopme.data.utils.requireData
import com.mtv.based.core.network.model.RequestOptions
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject

class ChatRemoteDataSource @Inject constructor(
    network: NetworkRepository
) : BaseRemoteDataSource(network) {

    suspend fun getChatList(asSeller: Boolean = false) =
        request<ApiResponse<ChatListResponse>>(
            endpoint = ApiEndPoint.Chat.GetList,
            options = RequestOptions(
                query = mapOf("asSeller" to asSeller.toString())
            )
        ).requireData()


    suspend fun getChats(id: String? = null, asSeller: Boolean = false) =
        request<ApiResponse<ChatResponse>>(
            endpoint = ApiEndPoint.Chat.Get,
            options = RequestOptions(
                query = buildMap {
                    put("asSeller", asSeller.toString())
                    id?.takeIf { it.isNotBlank() }?.let { put("id", it) }
                }
            )
        ).requireData()

    suspend fun sendMessage(body: ChatMessageSendRequest, asSeller: Boolean = false) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Chat.SendMessage,
            body = body,
            options = RequestOptions(
                query = mapOf("asSeller" to asSeller.toString())
            )
        ).requireData()

    suspend fun ensureConversation(cafeId: String) =
        request<ApiResponse<ChatConversationResponse>>(
            endpoint = ApiEndPoint.Chat.EnsureConversation,
            options = RequestOptions(
                query = mapOf("cafeId" to cafeId)
            )
        ).requireData()

    suspend fun readAll(body: ChatMessageMarkAsReadRequest, asSeller: Boolean = false) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Chat.MarkAllRead,
            body = body,
            options = RequestOptions(
                query = mapOf("asSeller" to asSeller.toString())
            )
        ).requireData()

    suspend fun clearAll(asSeller: Boolean = false) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Chat.Clear,
            options = RequestOptions(
                query = mapOf("asSeller" to asSeller.toString())
            )
        ).requireData()
}
