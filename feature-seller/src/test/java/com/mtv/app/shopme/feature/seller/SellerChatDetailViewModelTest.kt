package com.mtv.app.shopme.feature.seller

import androidx.lifecycle.SavedStateHandle
import com.mtv.app.shopme.domain.model.ChatList
import com.mtv.app.shopme.domain.model.ChatListItem
import com.mtv.app.shopme.domain.usecase.ChatMessageMarkAsReadUseCase
import com.mtv.app.shopme.domain.usecase.CreateChatMessageSendUseCase
import com.mtv.app.shopme.domain.usecase.GetChatListUseCase
import com.mtv.app.shopme.domain.usecase.GetChatMessageUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerChatDetailViewModel
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.provider.utils.SessionManager
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SellerChatDetailViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getChatMessageUseCase: GetChatMessageUseCase = mockk()
    private val getChatListUseCase: GetChatListUseCase = mockk()
    private val sendUseCase: CreateChatMessageSendUseCase = mockk(relaxed = true)
    private val markReadUseCase: ChatMessageMarkAsReadUseCase = mockk()
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Test
    fun `load should read seller conversation and map seller message state`() = runTest {
        every { getChatListUseCase.invoke(true) } returns flowOf(
            Resource.Success(
                ChatList(
                    listOf(
                        ChatListItem(
                            id = "conv-1",
                            name = "Raka Pratama",
                            lastMessage = "Pesanan siap dikirim",
                            time = "10:31",
                            unreadCount = 0,
                            avatarBase64 = "data:image/png;base64,CUSTOMER",
                            isFromUser = false
                        )
                    )
                )
            )
        )
        every { getChatMessageUseCase.invoke("conv-1", true) } returns flowOf(
            Resource.Success(
                listOf(
                    ChatListItem(
                        id = "conv-1",
                        name = "",
                        lastMessage = "Pesanan siap dikirim",
                        time = "",
                        unreadCount = 0,
                        avatarBase64 = null,
                        isFromUser = false
                    )
                )
            )
        )
        every { markReadUseCase.invoke("conv-1", true) } returns flowOf(Resource.Success(Unit))

        val vm = SellerChatDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("chatId" to "conv-1")),
            getChatListUseCase = getChatListUseCase,
            getChatMessageUseCase = getChatMessageUseCase,
            sendChatMessageUseCase = sendUseCase,
            chatMessageMarkAsReadUseCase = markReadUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(SellerChatDetailEvent.Load)
        advanceUntilIdle()

        assertEquals("conv-1", vm.uiState.value.activeChatId)
        assertEquals("Raka Pratama", vm.uiState.value.chatName)
        assertEquals("data:image/png;base64,CUSTOMER", vm.uiState.value.chatAvatarBase64)
        assertEquals(1, vm.uiState.value.messages.size)
        assertTrue(vm.uiState.value.messages.first().isFromSeller)
    }
}
