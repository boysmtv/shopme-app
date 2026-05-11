/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatRepository.kt
 *
 * Last modified by Dedy Wijaya on 30/03/26 16.39
 */

package com.mtv.app.shopme.domain.repository

import com.mtv.app.shopme.domain.model.ChatList
import com.mtv.app.shopme.domain.model.ChatListItem
import com.mtv.based.core.network.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChatList(asSeller: Boolean = false): Flow<Resource<ChatList>>

    fun getChats(chatId: String? = null, asSeller: Boolean = false): Flow<Resource<List<ChatListItem>>>

    fun ensureConversation(cafeId: String): Flow<Resource<String>>

    fun sendMessage(id: String, message: String, asSeller: Boolean = false): Flow<Resource<Unit>>

    fun readAllMessage(id: String, asSeller: Boolean = false): Flow<Resource<Unit>>

    fun clearAllMessages(asSeller: Boolean = false): Flow<Resource<Unit>>
}
