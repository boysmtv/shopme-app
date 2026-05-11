package com.mtv.app.shopme.feature.customer

import com.mtv.app.shopme.core.realtime.ShopmeRealtimeEvent
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeGateway
import com.mtv.app.shopme.domain.model.ChatList
import com.mtv.app.shopme.domain.model.ChatListItem
import com.mtv.app.shopme.domain.usecase.ClearChatListUseCase
import com.mtv.app.shopme.domain.usecase.GetChatListUseCase
import com.mtv.app.shopme.feature.customer.contract.ChatListEvent
import com.mtv.app.shopme.feature.customer.presentation.ChatListViewModel
import com.mtv.based.core.network.utils.LoadState
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
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ChatListViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getChatListUseCase: GetChatListUseCase = mockk()
    private val clearChatListUseCase: ClearChatListUseCase = mockk()
    private val realtimeEvents = MutableSharedFlow<ShopmeRealtimeEvent>(extraBufferCapacity = 4)
    private val realtimeGateway: ShopmeRealtimeGateway = mockk(relaxed = true)
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Test
    fun `clear should empty customer chat list state`() = runTest {
        every { realtimeGateway.events } returns realtimeEvents
        every { getChatListUseCase.invoke(false) } returns flowOf(
            Resource.Success(
                ChatList(
                    chatList = listOf(
                        ChatListItem(
                            id = "conv-1",
                            name = "Cafe Kopi Kita",
                            lastMessage = "Halo buyer",
                            time = "10:30",
                            unreadCount = 1,
                            avatarBase64 = null
                        )
                    )
                )
            )
        )
        every { clearChatListUseCase.invoke(false) } returns flowOf(Resource.Success(Unit))

        val vm = ChatListViewModel(getChatListUseCase, clearChatListUseCase, realtimeGateway, sessionManager)
        vm.onEvent(ChatListEvent.Load)
        advanceUntilIdle()

        vm.onEvent(ChatListEvent.ClickClearAll)
        advanceUntilIdle()

        val state = vm.uiState.value.chatListState as LoadState.Success
        assertEquals(0, state.data.chatList.size)
        verify(atLeast = 1) { realtimeGateway.ensureConnected() }
    }

    @Test
    fun `load should expose empty success state when backend has no chats`() = runTest {
        every { realtimeGateway.events } returns realtimeEvents
        every { getChatListUseCase.invoke(false) } returns flowOf(
            Resource.Success(ChatList(chatList = emptyList()))
        )

        val vm = ChatListViewModel(getChatListUseCase, clearChatListUseCase, realtimeGateway, sessionManager)
        vm.onEvent(ChatListEvent.Load)
        advanceUntilIdle()

        val state = vm.uiState.value.chatListState as LoadState.Success
        assertTrue(state.data.chatList.isEmpty())
    }
}
