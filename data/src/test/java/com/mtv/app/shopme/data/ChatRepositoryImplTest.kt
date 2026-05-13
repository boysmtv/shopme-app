package com.mtv.app.shopme.data

import app.cash.turbine.test
import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.database.entity.ChatListCacheEntity
import com.mtv.app.shopme.core.error.ErrorMapper
import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.remote.datasource.ChatRemoteDataSource
import com.mtv.app.shopme.data.remote.response.ChatListItemResponse
import com.mtv.app.shopme.data.remote.response.ChatListResponse
import com.mtv.app.shopme.data.repository.ChatRepositoryImpl
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.network.utils.UiError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatRepositoryImplTest {

    private val remote: ChatRemoteDataSource = mockk()
    private val homeDao: HomeDao = mockk(relaxed = true)
    private val errorMapper: ErrorMapper = mockk()

    private val repository = ChatRepositoryImpl(
        remote = remote,
        resultFlow = ResultFlowFactory(errorMapper),
        homeDao = homeDao,
        errorMapper = errorMapper
    )

    @Test
    fun `getChatList should emit cached chats first then refresh from backend`() = runTest {
        coEvery { homeDao.getChatListOnce("customer") } returns listOf(
            ChatListCacheEntity(
                cacheKey = "customer:chat-1",
                scope = "customer",
                conversationId = "chat-1",
                name = "Cached Cafe",
                lastMessage = "Cached message",
                time = "09:00",
                unreadCount = 1,
                avatarUrl = "https://media.shopme.test/chat/cached-avatar.jpg",
                updatedAt = 1L
            )
        )
        coEvery { remote.getChatList(false) } returns ChatListResponse(
            chatList = listOf(
                ChatListItemResponse(
                    id = "chat-2",
                    name = "Remote Cafe",
                    lastMessage = "Remote message",
                    time = "10:00",
                    unreadCount = 2,
                avatar = "remote-avatar"
            )
        )
        )

        repository.getChatList(false).test {
            assertEquals(Resource.Loading, awaitItem())

            val cached = awaitItem() as Resource.Success
            assertEquals("Cached Cafe", cached.data.chatList.first().name)

            val refreshed = awaitItem() as Resource.Success
            assertEquals("Remote Cafe", refreshed.data.chatList.first().name)

            awaitComplete()
        }

        coVerify { homeDao.clearChatList("customer") }
        coVerify { homeDao.insertChatList(match { it.single().conversationId == "chat-2" }) }
    }

    @Test
    fun `getChatList should emit mapped error when cache empty and backend fails`() = runTest {
        val mappedError = mockk<UiError>(relaxed = true)
        coEvery { homeDao.getChatListOnce("seller") } returns emptyList()
        coEvery { remote.getChatList(true) } throws IllegalStateException("backend down")
        every { errorMapper.map(any()) } returns mappedError

        repository.getChatList(true).test {
            assertEquals(Resource.Loading, awaitItem())
            val error = awaitItem() as Resource.Error
            assertEquals(mappedError, error.error)
            awaitComplete()
        }
    }

    @Test
    fun `clearAllMessages should clear local chat cache for seller scope`() = runTest {
        coEvery { remote.clearAll(true) } returns Unit

        repository.clearAllMessages(true).test {
            assertEquals(Resource.Loading, awaitItem())
            awaitItem()
            awaitComplete()
        }

        coVerify { homeDao.clearChatList("seller") }
    }
}
