/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatMessageMarkAsReadUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 13.45
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.based.core.provider.based.BaseUseCase
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.request.ChatMessageMarkAsReadRequest
import com.mtv.based.core.network.di.IoDispatcher
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class ChatMessageMarkAsReadUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val repository: NetworkRepository,
) : BaseUseCase<ChatMessageMarkAsReadRequest, ApiResponse<Unit>>(dispatcher) {

    override suspend fun execute(param: ChatMessageMarkAsReadRequest) = repository.request<ApiResponse<Unit>>(
        endpoint = ApiEndPoint.PostChatMessageAllRead,
        body = param
    )

}