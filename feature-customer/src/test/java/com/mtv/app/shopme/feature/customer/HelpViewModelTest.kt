package com.mtv.app.shopme.feature.customer

import com.mtv.app.shopme.domain.model.SupportBootstrapMessage
import com.mtv.app.shopme.domain.model.SupportCenter
import com.mtv.app.shopme.domain.model.SupportFaq
import com.mtv.app.shopme.domain.usecase.GetSupportCenterUseCase
import com.mtv.app.shopme.feature.customer.contract.HelpEvent
import com.mtv.app.shopme.feature.customer.presentation.HelpViewModel
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SessionManager
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HelpViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getSupportCenterUseCase: GetSupportCenterUseCase = mockk()
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Test
    fun `load should reflect backend faq`() = runTest {
        every { getSupportCenterUseCase.invoke() } returns flowOf(Resource.Success(supportCenter()))

        val vm = HelpViewModel(getSupportCenterUseCase, sessionManager)
        vm.onEvent(HelpEvent.Load)
        advanceUntilIdle()

        assertEquals(2, vm.uiState.value.faq.size)
        assertEquals("Bagaimana cara melacak pesanan?", vm.uiState.value.faq.first().question)
    }

    @Test
    fun `unauthorized help load should force logout`() = runTest {
        every { getSupportCenterUseCase.invoke() } returns flowOf(
            Resource.Error(UiError.Unauthorized("Session expired"))
        )

        val vm = HelpViewModel(getSupportCenterUseCase, sessionManager)
        vm.onEvent(HelpEvent.Load)
        advanceUntilIdle()

        coVerify(exactly = 1) { sessionManager.forceLogout() }
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
        faq = listOf(
            SupportFaq("faq-1", "Bagaimana cara melacak pesanan?", "Masuk ke menu pesanan."),
            SupportFaq("faq-2", "Metode pembayaran apa saja?", "Transfer bank tersedia.")
        ),
        bootstrapMessages = listOf(
            SupportBootstrapMessage("msg-1", "Halo 👋 Ada yang bisa kami bantu?", false, "10:00")
        ),
        sellerTerms = emptyList()
    )
}
