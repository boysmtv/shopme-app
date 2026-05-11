package com.mtv.app.shopme.feature.customer

import android.content.Intent
import com.mtv.app.shopme.domain.model.SupportBootstrapMessage
import com.mtv.app.shopme.domain.model.SupportCenter
import com.mtv.app.shopme.domain.model.SupportFaq
import com.mtv.app.shopme.domain.usecase.GetSupportCenterUseCase
import com.mtv.app.shopme.feature.customer.contract.SupportEffect
import com.mtv.app.shopme.feature.customer.contract.SupportEvent
import com.mtv.app.shopme.feature.customer.presentation.SupportViewModel
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.provider.utils.SessionManager
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SupportViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getSupportCenterUseCase: GetSupportCenterUseCase = mockk()
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Test
    fun `open whatsapp should emit backend whatsapp intent`() = runTest {
        every { getSupportCenterUseCase.invoke() } returns flowOf(Resource.Success(supportCenter()))

        val vm = SupportViewModel(getSupportCenterUseCase, sessionManager)
        vm.onEvent(SupportEvent.Load)
        advanceUntilIdle()
        val effect = async { vm.effect.first() as SupportEffect.OpenIntent }

        vm.onEvent(SupportEvent.OpenWhatsapp)
        advanceUntilIdle()

        val result = effect.await().intent
        assertEquals(Intent.ACTION_VIEW, result.action)
        assertTrue(result.data.toString().startsWith("https://wa.me/6281234567890"))
    }

    @Test
    fun `open email should emit backend mailto intent`() = runTest {
        every { getSupportCenterUseCase.invoke() } returns flowOf(Resource.Success(supportCenter()))

        val vm = SupportViewModel(getSupportCenterUseCase, sessionManager)
        vm.onEvent(SupportEvent.Load)
        advanceUntilIdle()
        val effect = async { vm.effect.first() as SupportEffect.OpenIntent }

        vm.onEvent(SupportEvent.OpenEmail)
        advanceUntilIdle()

        val result = effect.await().intent
        assertEquals(Intent.ACTION_SENDTO, result.action)
        assertTrue(result.data.toString().startsWith("mailto:support@shopme.local"))
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
}
