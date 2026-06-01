/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatMessageUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 13.45
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.model.ChatMessage
import com.mtv.app.shopme.domain.model.PagedData
import com.mtv.app.shopme.domain.repository.ChatRepository
import com.mtv.based.core.network.utils.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetChatMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(chatId: String? = null, asSeller: Boolean = false): Flow<Resource<List<ChatMessage>>> =
        repository.getChats(chatId, asSeller)

    fun page(
        chatId: String,
        asSeller: Boolean = false,
        page: Int,
        size: Int
    ): Flow<Resource<PagedData<ChatMessage>>> = repository.getChatsPage(chatId, asSeller, page, size)
}
