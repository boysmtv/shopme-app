package com.mtv.app.shopme.feature.customer

import androidx.lifecycle.SavedStateHandle
import com.mtv.app.shopme.domain.model.ChatListItem
import com.mtv.app.shopme.domain.usecase.ChatMessageMarkAsReadUseCase
import com.mtv.app.shopme.domain.usecase.CreateChatMessageSendUseCase
import com.mtv.app.shopme.domain.usecase.GetChatMessageUseCase
import com.mtv.app.shopme.feature.customer.contract.ChatEvent
import com.mtv.app.shopme.feature.customer.presentation.ChatViewModel
import com.mtv.based.core.network.utils.Resource
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ChatViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getChatMessageUseCase: GetChatMessageUseCase = mockk()
    private val sendUseCase: CreateChatMessageSendUseCase = mockk(relaxed = true)
    private val markReadUseCase: ChatMessageMarkAsReadUseCase = mockk(relaxed = true)

    @Test
    fun `load should infer active chat and mark it as read`() = runTest {
        every { getChatMessageUseCase.invoke(null, false) } returns flowOf(
            Resource.Success(
                listOf(
                    ChatListItem(
                        id = "conv-1",
                        name = "Cafe Kopi Kita",
                        lastMessage = "Pesanan sedang diproses",
                        time = "10:30",
                        unreadCount = 1,
                        avatarBase64 = null,
                        isFromUser = false
                    )
                )
            )
        )
        every { markReadUseCase.invoke("conv-1", false) } returns flowOf(Resource.Success(Unit))

        val vm = ChatViewModel(
            savedStateHandle = SavedStateHandle(),
            chatMessageUseCase = getChatMessageUseCase,
            chatSendMessageUseCase = sendUseCase,
            chatMessageMarkAsReadUseCase = markReadUseCase
        )

        vm.onEvent(ChatEvent.Load)
        advanceUntilIdle()

        assertEquals("conv-1", vm.uiState.value.activeChatId)
    }
}
