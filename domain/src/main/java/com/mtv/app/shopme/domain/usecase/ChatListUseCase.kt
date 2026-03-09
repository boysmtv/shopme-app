/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatListUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 13.42
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.core.provider.based.BaseUseCase
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.ChatListResponse
import com.mtv.based.core.network.di.IoDispatcher
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class ChatListUseCase @Inject constructor(
    private val repository: NetworkRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : BaseUseCase<Unit, ApiResponse<ChatListResponse>>(dispatcher) {

    override suspend fun execute(param: Unit) = repository.request<ApiResponse<ChatListResponse>>(
        endpoint = ApiEndPoint.GetChatList,
    )

}