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
    fun getChatList(): Flow<Resource<ChatList>>

    fun getChats(): Flow<Resource<List<ChatListItem>>>

    fun sendMessage(id: String, message: String): Flow<Resource<Unit>>

    fun readAllMessage(id: String): Flow<Resource<Unit>>
}