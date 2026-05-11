package com.mtv.app.shopme.feature.seller

import com.mtv.app.shopme.core.realtime.ShopmeRealtimeEvent
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeGateway
import com.mtv.app.shopme.domain.model.ChatList
import com.mtv.app.shopme.domain.model.ChatListItem
import com.mtv.app.shopme.domain.usecase.ClearChatListUseCase
import com.mtv.app.shopme.domain.usecase.GetChatListUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerChatListEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerChatListViewModel
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

class SellerChatListViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()
    private val useCase: GetChatListUseCase = mockk()
    private val clearChatListUseCase: ClearChatListUseCase = mockk(relaxed = true)
    private val realtimeEvents = MutableSharedFlow<ShopmeRealtimeEvent>(extraBufferCapacity = 4)
    private val realtimeGateway: ShopmeRealtimeGateway = mockk(relaxed = true)
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Test
    fun `load should request seller chat list and expose mapped items`() = runTest {
        every { realtimeGateway.events } returns realtimeEvents
        every { useCase.invoke(true) } returns flowOf(
            Resource.Success(
                ChatList(
                    chatList = listOf(
                        ChatListItem(
                            id = "conv-1",
                            name = "Dedy",
                            lastMessage = "Pesanan siap",
                            time = "10:30",
                            unreadCount = 2,
                            avatarBase64 = null
                        )
                    )
                )
            )
        )

        val vm = SellerChatListViewModel(useCase, clearChatListUseCase, realtimeGateway, sessionManager)
        vm.onEvent(SellerChatListEvent.Load)
        advanceUntilIdle()

        assertEquals(1, vm.uiState.value.chatList.size)
        assertEquals("conv-1", vm.uiState.value.chatList.first().id)
        assertEquals("Dedy", vm.uiState.value.chatList.first().name)
        verify(exactly = 1) { realtimeGateway.ensureConnected() }
    }

    @Test
    fun `clear should remove all seller chats from state`() = runTest {
        every { realtimeGateway.events } returns realtimeEvents
        every { useCase.invoke(true) } returns flowOf(
            Resource.Success(
                ChatList(
                    chatList = listOf(
                        ChatListItem(
                            id = "conv-1",
                            name = "Dedy",
                            lastMessage = "Pesanan siap",
                            time = "10:30",
                            unreadCount = 2,
                            avatarBase64 = null
                        )
                    )
                )
            )
        )
        every { clearChatListUseCase.invoke(true) } returns flowOf(Resource.Success(Unit))

        val vm = SellerChatListViewModel(useCase, clearChatListUseCase, realtimeGateway, sessionManager)
        vm.onEvent(SellerChatListEvent.Load)
        advanceUntilIdle()

        vm.onEvent(SellerChatListEvent.ClickClearAll)
        advanceUntilIdle()

        assertEquals(0, vm.uiState.value.chatList.size)
    }

    @Test
    fun `load should keep empty success state when seller has no chats`() = runTest {
        every { realtimeGateway.events } returns realtimeEvents
        every { useCase.invoke(true) } returns flowOf(
            Resource.Success(ChatList(chatList = emptyList()))
        )

        val vm = SellerChatListViewModel(useCase, clearChatListUseCase, realtimeGateway, sessionManager)
        vm.onEvent(SellerChatListEvent.Load)
        advanceUntilIdle()

        assertTrue(vm.uiState.value.chatList.isEmpty())
        assertEquals(false, vm.uiState.value.isLoading)
    }
}
