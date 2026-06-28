package com.mtv.app.shopme.domain.usecase

import app.cash.turbine.test
import com.mtv.app.shopme.domain.model.ChatList
import com.mtv.app.shopme.domain.model.ChatMessage
import com.mtv.app.shopme.domain.model.PagedData
import com.mtv.app.shopme.domain.repository.ChatRepository
import com.mtv.app.shopme.domain.model.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatUseCaseTest {

    private val repository: ChatRepository = mockk()
    private lateinit var getChatMessageUseCase: GetChatMessageUseCase
    private lateinit var createChatMessageSendUseCase: CreateChatMessageSendUseCase
    private lateinit var ensureChatConversationUseCase: EnsureChatConversationUseCase
    private lateinit var getChatListUseCase: GetChatListUseCase
    private lateinit var clearChatListUseCase: ClearChatListUseCase
    private lateinit var chatMessageMarkAsReadUseCase: ChatMessageMarkAsReadUseCase

    @Before
    fun setUp() {
        getChatMessageUseCase = GetChatMessageUseCase(repository)
        createChatMessageSendUseCase = CreateChatMessageSendUseCase(repository)
        ensureChatConversationUseCase = EnsureChatConversationUseCase(repository)
        getChatListUseCase = GetChatListUseCase(repository)
        clearChatListUseCase = ClearChatListUseCase(repository)
        chatMessageMarkAsReadUseCase = ChatMessageMarkAsReadUseCase(repository)
    }

    @Test
    fun `GetChatMessageUseCase invoke delegates to repository getChats`() = runTest {
        val chatId = "chat-1"
        coEvery { repository.getChats(chatId, false) } returns flowOf(Resource.Success(emptyList()))

        getChatMessageUseCase(chatId).test {
            assertEquals(Resource.Success(emptyList<ChatMessage>()), awaitItem())
            awaitComplete()
        }

        coVerify { repository.getChats(chatId, false) }
    }

    @Test
    fun `GetChatMessageUseCase page delegates to repository getChatsPage`() = runTest {
        val chatId = "chat-1"
        val page = 1
        val size = 20
        coEvery { repository.getChatsPage(chatId, false, page, size) } returns flowOf(
            Resource.Success(PagedData(content = emptyList(), page = page, last = true))
        )

        getChatMessageUseCase.page(chatId, false, page, size).test {
            assertEquals(
                Resource.Success(PagedData(content = emptyList<ChatMessage>(), page = page, last = true)),
                awaitItem()
            )
            awaitComplete()
        }

        coVerify { repository.getChatsPage(chatId, false, page, size) }
    }

    @Test
    fun `CreateChatMessageSendUseCase delegates to repository sendMessage`() = runTest {
        val id = "chat-1"
        val message = "Hello"
        coEvery { repository.sendMessage(id, message, false) } returns flowOf(Resource.Success(Unit))

        createChatMessageSendUseCase(id, message).test {
            assertEquals(Resource.Success(Unit), awaitItem())
            awaitComplete()
        }

        coVerify { repository.sendMessage(id, message, false) }
    }

    @Test
    fun `CreateChatMessageSendUseCase delegates asSeller param correctly`() = runTest {
        val id = "chat-1"
        val message = "Hello seller"
        coEvery { repository.sendMessage(id, message, true) } returns flowOf(Resource.Success(Unit))

        createChatMessageSendUseCase(id, message, asSeller = true).test {
            assertEquals(Resource.Success(Unit), awaitItem())
            awaitComplete()
        }

        coVerify { repository.sendMessage(id, message, true) }
    }

    @Test
    fun `EnsureChatConversationUseCase delegates to repository ensureConversation`() = runTest {
        val cafeId = "cafe-1"
        coEvery { repository.ensureConversation(cafeId) } returns flowOf(Resource.Success("chat-1"))

        ensureChatConversationUseCase(cafeId).test {
            assertEquals(Resource.Success("chat-1"), awaitItem())
            awaitComplete()
        }

        coVerify { repository.ensureConversation(cafeId) }
    }

    @Test
    fun `GetChatListUseCase delegates to repository getChatList`() = runTest {
        coEvery { repository.getChatList(false) } returns flowOf(Resource.Success(ChatList(chatList = emptyList())))

        getChatListUseCase().test {
            assertEquals(Resource.Success(ChatList(emptyList())), awaitItem())
            awaitComplete()
        }

        coVerify { repository.getChatList(false) }
    }

    @Test
    fun `ClearChatListUseCase delegates to repository clearAllMessages`() = runTest {
        coEvery { repository.clearAllMessages(false) } returns flowOf(Resource.Success(Unit))

        clearChatListUseCase().test {
            assertEquals(Resource.Success(Unit), awaitItem())
            awaitComplete()
        }

        coVerify { repository.clearAllMessages(false) }
    }

    @Test
    fun `ChatMessageMarkAsReadUseCase delegates to repository readAllMessage`() = runTest {
        val id = "chat-1"
        coEvery { repository.readAllMessage(id, false) } returns flowOf(Resource.Success(Unit))

        chatMessageMarkAsReadUseCase(id).test {
            assertEquals(Resource.Success(Unit), awaitItem())
            awaitComplete()
        }

        coVerify { repository.readAllMessage(id, false) }
    }
}
