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
import com.mtv.app.shopme.data.mapper.toChatMessage
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.mapper.toEntity
import com.mtv.app.shopme.data.mapper.toMessageEntity
import com.mtv.app.shopme.data.remote.datasource.ChatRemoteDataSource
import com.mtv.app.shopme.data.remote.response.PageResponse
import com.mtv.app.shopme.data.remote.request.ChatMessageMarkAsReadRequest
import com.mtv.app.shopme.data.remote.request.ChatMessageSendRequest
import com.mtv.app.shopme.data.remote.response.ChatItem
import com.mtv.app.shopme.domain.repository.ChatRepository
import com.mtv.app.shopme.domain.model.ChatListItem
import com.mtv.app.shopme.domain.model.ChatMessage
import com.mtv.app.shopme.domain.model.PagedData
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
            val cached = homeDao.getChatListOnce(scope).map { it.toDomain() }.sortedByDescending { it.time }
            if (cached.isNotEmpty()) {
                emit(Resource.Success(com.mtv.app.shopme.domain.model.ChatList(cached)))
            }

            try {
                val remoteChatList = remote.getChatList(asSeller).toDomain().let {
                    it.copy(chatList = it.chatList.sortedByDescending { it.time })
                }
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
        flow {
            emit(Resource.Loading)
            val conversationId = chatId.orEmpty()
            val scope = if (asSeller) SELLER_SCOPE else CUSTOMER_SCOPE
            val cached = if (conversationId.isNotBlank()) {
                homeDao.getChatMessagesOnce(scope, conversationId).map { it.toChatMessage() }
            } else {
                emptyList()
            }
            if (cached.isNotEmpty()) {
                emit(Resource.Success(cached))
            }

            try {
                val remoteChats = if (conversationId.isNotBlank()) {
                    loadLatestRemoteChats(conversationId, asSeller, CHAT_PAGE_SIZE)
                } else {
                    remote.getChats(chatId, asSeller).chatList.map { (it as ChatItem).toChatMessage() }
                }
                if (conversationId.isNotBlank()) {
                    homeDao.clearChatMessages(scope, conversationId)
                    homeDao.insertChatMessages(
                        remoteChats.mapIndexed { index, item ->
                            item.toMessageEntity(scope, conversationId, index)
                        }
                    )
                }
                emit(Resource.Success(remoteChats))
            } catch (throwable: Throwable) {
                if (cached.isEmpty()) {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun getChatsPage(
        chatId: String,
        asSeller: Boolean,
        page: Int,
        size: Int
    ) = flow {
        emit(Resource.Loading)
        val scope = if (asSeller) SELLER_SCOPE else CUSTOMER_SCOPE
        val cached = if (page < 0) {
            homeDao.getChatMessagesOnce(scope, chatId).map { it.toChatMessage() }
        } else {
            emptyList()
        }
        if (cached.isNotEmpty()) {
            emit(Resource.Success(PagedData(content = cached, page = page, last = false)))
        }

        try {
            val response = if (page < 0) {
                loadLatestRemotePage(chatId, asSeller, size)
            } else {
                remote.getChatsPage(chatId, asSeller, page, size)
            }
            val messages = response.content.map { it.toChatMessage() }
            if (page < 0) {
                homeDao.clearChatMessages(scope, chatId)
            }
            homeDao.insertChatMessages(
                messages.mapIndexed { index, item ->
                    item.toMessageEntity(scope, chatId, response.page * response.size + index)
                }
            )
            emit(Resource.Success(response.toPagedDomain(messages)))
        } catch (throwable: Throwable) {
            emit(Resource.Error(errorMapper.map(throwable)))
        }
    }.flowOn(Dispatchers.IO)

    override fun ensureConversation(cafeId: String) =
        resultFlow.create {
            remote.ensureConversation(cafeId).id
        }

    override fun ensureOrderConversation(orderId: String) =
        resultFlow.create {
            remote.ensureOrderConversation(orderId).id
        }

    override fun ensureSellerConversation(orderId: String) =
        resultFlow.create {
            remote.ensureSellerConversation(orderId).id
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
            val scope = if (asSeller) SELLER_SCOPE else CUSTOMER_SCOPE
            homeDao.clearChatList(scope)
            homeDao.clearChatMessagesByScope(scope)
        }

    companion object {
        private const val CUSTOMER_SCOPE = "customer"
        private const val SELLER_SCOPE = "seller"
        private const val CHAT_PAGE_SIZE = 30
    }

    private suspend fun loadLatestRemoteChats(
        conversationId: String,
        asSeller: Boolean,
        size: Int
    ): List<ChatMessage> =
        loadLatestRemotePage(conversationId, asSeller, size)
            .content
            .map { it.toChatMessage() }

    private suspend fun loadLatestRemotePage(
        conversationId: String,
        asSeller: Boolean,
        size: Int
    ): PageResponse<ChatItem> {
        val firstPage = remote.getChatsPage(conversationId, asSeller, page = 0, size = size)
        if (firstPage.last) {
            return firstPage
        }

        val latestPageIndex = (firstPage.totalPages - 1).coerceAtLeast(0)
        return remote.getChatsPage(conversationId, asSeller, page = latestPageIndex, size = size)
    }

    private fun PageResponse<ChatItem>.toPagedDomain(
        messages: List<ChatMessage> = content.map { it.toChatMessage() }
    ): PagedData<ChatMessage> = PagedData(
        content = messages,
        page = page,
        last = page <= 0
    )

    private fun String.normalizedChatTimeKey(): String =
        trim()
            .replace("T", " ")
            .substringBefore(".")
}
