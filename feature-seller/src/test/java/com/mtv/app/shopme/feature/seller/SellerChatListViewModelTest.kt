package com.mtv.app.shopme.feature.seller

import com.mtv.app.shopme.domain.model.ChatList
import com.mtv.app.shopme.domain.model.ChatListItem
import com.mtv.app.shopme.domain.usecase.GetChatListUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerChatListEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerChatListViewModel
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.provider.utils.SessionManager
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SellerChatListViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()
    private val useCase: GetChatListUseCase = mockk()
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Test
    fun `load should request seller chat list and expose mapped items`() = runTest {
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

        val vm = SellerChatListViewModel(useCase, sessionManager)
        vm.onEvent(SellerChatListEvent.Load)
        advanceUntilIdle()

        assertEquals(1, vm.uiState.value.chatList.size)
        assertEquals("conv-1", vm.uiState.value.chatList.first().id)
        assertEquals("Dedy", vm.uiState.value.chatList.first().name)
    }
}
