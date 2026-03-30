/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatRepository.kt
 *
 * Last modified by Dedy Wijaya on 30/03/26 16.39
 */

package com.mtv.app.shopme.domain.repository

import com.mtv.app.shopme.domain.model.ChatList
import com.mtv.based.core.network.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChatList(): Flow<Resource<ChatList>>
}