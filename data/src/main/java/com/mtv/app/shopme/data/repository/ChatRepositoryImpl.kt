/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatRepositoryImpl.kt
 *
 * Last modified by Dedy Wijaya on 30/03/26 16.39
 */

package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.error.ErrorMapper
import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.mapper.toEntity
import com.mtv.app.shopme.data.remote.datasource.ChatRemoteDataSource
import com.mtv.app.shopme.data.remote.request.ChatMessageMarkAsReadRequest
import com.mtv.app.shopme.data.remote.request.ChatMessageSendRequest
import com.mtv.app.shopme.domain.repository.ChatRepository
import com.mtv.based.core.network.utils.Resource
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ChatRepositoryImpl @Inject constructor(
    private val remote: ChatRemoteDataSource,
    private val resultFlow: ResultFlowFactory,
    private val homeDao: HomeDao,
    private val errorMapper: ErrorMapper
) : ChatRepository {

    override fun getChatList(asSeller: Boolean) =
        flow {
            emit(Resource.Loading)
            val scope = if (asSeller) SELLER_SCOPE else CUSTOMER_SCOPE
            val cached = homeDao.getChatListOnce(scope).map { it.toDomain() }
            if (cached.isNotEmpty()) {
                emit(Resource.Success(com.mtv.app.shopme.domain.model.ChatList(cached)))
            }

            try {
                val remoteChatList = remote.getChatList(asSeller).toDomain()
                homeDao.clearChatList(scope)
                homeDao.insertChatList(remoteChatList.chatList.map { it.toEntity(scope) })
                emit(Resource.Success(remoteChatList))
            } catch (throwable: Throwable) {
                if (cached.isEmpty()) {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)


    override fun getChats(chatId: String?, asSeller: Boolean) =
        resultFlow.create {
            remote.getChats(chatId, asSeller).toDomain()
        }

    override fun ensureConversation(cafeId: String) =
        resultFlow.create {
            remote.ensureConversation(cafeId).id
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
            homeDao.clearChatList(if (asSeller) SELLER_SCOPE else CUSTOMER_SCOPE)
        }

    companion object {
        private const val CUSTOMER_SCOPE = "customer"
        private const val SELLER_SCOPE = "seller"
    }
}
