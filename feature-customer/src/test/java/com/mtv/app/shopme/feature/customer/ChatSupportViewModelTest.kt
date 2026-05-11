package com.mtv.app.shopme.feature.customer

import com.mtv.app.shopme.domain.model.SupportBootstrapMessage
import com.mtv.app.shopme.domain.model.SupportCenter
import com.mtv.app.shopme.domain.model.SupportChat
import com.mtv.app.shopme.domain.model.SupportChatMessage
import com.mtv.app.shopme.domain.model.SupportFaq
import com.mtv.app.shopme.domain.usecase.GetSupportCenterUseCase
import com.mtv.app.shopme.domain.usecase.GetSupportChatUseCase
import com.mtv.app.shopme.domain.usecase.SendSupportChatMessageUseCase
import com.mtv.app.shopme.feature.customer.contract.ChatSupportEvent
import com.mtv.app.shopme.feature.customer.presentation.ChatSupportViewModel
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
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ChatSupportViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getSupportCenterUseCase: GetSupportCenterUseCase = mockk()
    private val getSupportChatUseCase: GetSupportChatUseCase = mockk()
    private val sendSupportChatMessageUseCase: SendSupportChatMessageUseCase = mockk()
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Test
    fun `load should hydrate metadata and backend chat messages`() = runTest {
        every { getSupportCenterUseCase.invoke() } returns flowOf(Resource.Success(supportCenter()))
        every { getSupportChatUseCase.invoke() } returns flowOf(Resource.Success(initialChat()))

        val vm = ChatSupportViewModel(
            getSupportCenterUseCase,
            getSupportChatUseCase,
            sendSupportChatMessageUseCase,
            sessionManager
        )
        vm.onEvent(ChatSupportEvent.Load)
        advanceUntilIdle()

        assertEquals("Support Agent", vm.uiState.value.title)
        assertEquals(1, vm.uiState.value.messages.size)
        assertEquals("Halo 👋 Ada yang bisa kami bantu?", vm.uiState.value.messages.first().message)
    }

    @Test
    fun `send message should update conversation from backend`() = runTest {
        every { getSupportCenterUseCase.invoke() } returns flowOf(Resource.Success(supportCenter()))
        every { getSupportChatUseCase.invoke() } returns flowOf(Resource.Success(initialChat()))
        every { sendSupportChatMessageUseCase.invoke("butuh bantuan order") } returns flowOf(
            Resource.Success(
                SupportChat(
                    messages = listOf(
                        SupportChatMessage("msg-1", "Halo 👋 Ada yang bisa kami bantu?", false, "10:00"),
                        SupportChatMessage("msg-2", "butuh bantuan order", true, "10:01"),
                        SupportChatMessage("msg-3", "Pesan Anda sudah kami terima.", false, "10:01")
                    )
                )
            )
        )

        val vm = ChatSupportViewModel(
            getSupportCenterUseCase,
            getSupportChatUseCase,
            sendSupportChatMessageUseCase,
            sessionManager
        )
        vm.onEvent(ChatSupportEvent.Load)
        advanceUntilIdle()
        vm.onEvent(ChatSupportEvent.OnMessageChange("butuh bantuan order"))
        vm.onEvent(ChatSupportEvent.SendMessage)
        advanceUntilIdle()

        assertEquals("", vm.uiState.value.currentMessage)
        assertEquals(3, vm.uiState.value.messages.size)
        assertEquals("butuh bantuan order", vm.uiState.value.messages[1].message)
        assertEquals(false, vm.uiState.value.isAgentTyping)
    }

    private fun supportCenter() = SupportCenter(
        phone = "081234567890",
        email = "support@shopme.local",
        whatsapp = "6281234567890",
        whatsappMessageTemplate = "Halo tim Shopme, saya butuh bantuan.",
        emailSubject = "Support Request",
        emailBodyTemplate = "Mohon bantuan terkait aplikasi Shopme.",
        operationalStartHour = 8,
        operationalEndHour = 22,
        operationalTimezone = "Asia/Jakarta",
        operationalHoursLabel = "Senin - Minggu • 08:00 - 22:00",
        statusOnlineLabel = "Online • Respon < 2 menit",
        statusOfflineLabel = "Offline • Buka 08:00",
        liveChatTitle = "Support Agent",
        liveChatStatusOnlineLabel = "Online • Respon cepat",
        sellerOnboardingTitle = "Create Your Cafe",
        sellerOnboardingDescription = "Read and agree before continuing.",
        sellerOnboardingFooter = "By continuing, you agree.",
        faq = listOf(SupportFaq("faq-1", "Q", "A")),
        bootstrapMessages = listOf(
            SupportBootstrapMessage("msg-1", "Halo 👋 Ada yang bisa kami bantu?", false, "10:00")
        ),
        sellerTerms = emptyList()
    )

    private fun initialChat() = SupportChat(
        messages = listOf(
            SupportChatMessage("msg-1", "Halo 👋 Ada yang bisa kami bantu?", false, "10:00")
        )
    )
}
