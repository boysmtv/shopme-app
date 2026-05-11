/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatRepositoryImpl.kt
 *
 * Last modified by Dedy Wijaya on 30/03/26 16.39
 */

package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.remote.datasource.ChatRemoteDataSource
import com.mtv.app.shopme.data.remote.request.ChatMessageMarkAsReadRequest
import com.mtv.app.shopme.data.remote.request.ChatMessageSendRequest
import com.mtv.app.shopme.domain.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val remote: ChatRemoteDataSource,
    private val resultFlow: ResultFlowFactory
) : ChatRepository {

    override fun getChatList(asSeller: Boolean) =
        resultFlow.create {
            remote.getChatList(asSeller).toDomain()
        }


    override fun getChats(chatId: String?, asSeller: Boolean) =
        resultFlow.create {
            remote.getChats(chatId, asSeller).toDomain()
        }

    override fun sendMessage(id: String, message: String, asSeller: Boolean) =
        resultFlow.create {
            remote.sendMessage(
                body = ChatMessageSendRequest(id, message),
                asSeller = asSeller
            )
        }

    override fun readAllMessage(id: String, asSeller: Boolean) =
        resultFlow.create {
            remote.readAll(
                body = ChatMessageMarkAsReadRequest(id, ""),
                asSeller = asSeller
            )
        }

    override fun clearAllMessages(asSeller: Boolean) =
        resultFlow.create {
            remote.clearAll(asSeller)
        }
}
