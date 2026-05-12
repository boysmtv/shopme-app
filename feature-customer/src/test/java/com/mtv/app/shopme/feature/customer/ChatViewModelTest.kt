package com.mtv.app.shopme.feature.customer

import androidx.lifecycle.SavedStateHandle
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeEvent
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeGateway
import com.mtv.app.shopme.domain.model.ChatListItem
import com.mtv.app.shopme.domain.usecase.ChatMessageMarkAsReadUseCase
import com.mtv.app.shopme.domain.usecase.CreateChatMessageSendUseCase
import com.mtv.app.shopme.domain.usecase.GetChatListUseCase
import com.mtv.app.shopme.domain.usecase.GetChatMessageUseCase
import com.mtv.app.shopme.domain.model.ChatList
import com.mtv.app.shopme.feature.customer.contract.ChatEvent
import com.mtv.app.shopme.feature.customer.presentation.ChatViewModel
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.provider.utils.SessionManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ChatViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getChatMessageUseCase: GetChatMessageUseCase = mockk()
    private val getChatListUseCase: GetChatListUseCase = mockk()
    private val sendUseCase: CreateChatMessageSendUseCase = mockk(relaxed = true)
    private val markReadUseCase: ChatMessageMarkAsReadUseCase = mockk(relaxed = true)
    private val realtimeEvents = MutableSharedFlow<ShopmeRealtimeEvent>(extraBufferCapacity = 4)
    private val realtimeGateway: ShopmeRealtimeGateway = mockk(relaxed = true)
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Test
    fun `load should infer active chat and mark it as read`() = runTest {
        every { realtimeGateway.events } returns realtimeEvents
        every { getChatListUseCase.invoke(false) } returns flowOf(
            Resource.Success(
                ChatList(
                    listOf(
                        ChatListItem(
                            id = "conv-1",
                            name = "Cafe Kopi Kita",
                            lastMessage = "Pesanan sedang diproses",
                            time = "10:30",
                            unreadCount = 1,
                            avatarUrl = "https://media.shopme.test/cafes/cafe-1/thumb.jpg",
                            isFromUser = false
                        )
                    )
                )
            )
        )
        every { getChatMessageUseCase.invoke("conv-1", false) } returns flowOf(
            Resource.Success(
                listOf(
                    ChatListItem(
                        id = "conv-1",
                        name = "Cafe Kopi Kita",
                        lastMessage = "Pesanan sedang diproses",
                        time = "10:30",
                        unreadCount = 1,
                        avatarUrl = null,
                        isFromUser = false
                    )
                )
            )
        )
        every { getChatMessageUseCase.invoke(null, false) } returns flowOf(Resource.Success(emptyList()))
        every { markReadUseCase.invoke("conv-1", false) } returns flowOf(Resource.Success(Unit))

        val vm = ChatViewModel(
            savedStateHandle = SavedStateHandle(),
            chatListUseCase = getChatListUseCase,
            chatMessageUseCase = getChatMessageUseCase,
            chatSendMessageUseCase = sendUseCase,
            chatMessageMarkAsReadUseCase = markReadUseCase,
            realtimeGateway = realtimeGateway,
            sessionManager = sessionManager
        )

        vm.onEvent(ChatEvent.Load)
        advanceUntilIdle()

        assertEquals("conv-1", vm.uiState.value.activeChatId)
        assertEquals("Cafe Kopi Kita", vm.uiState.value.chatName)
        assertEquals("https://media.shopme.test/cafes/cafe-1/thumb.jpg", vm.uiState.value.chatAvatarUrl)
        verify(exactly = 1) { markReadUseCase.invoke("conv-1", false) }
        verify(exactly = 1) { realtimeGateway.ensureConnected() }
    }
}
