package com.mtv.app.shopme.feature.seller

import com.mtv.app.shopme.domain.model.SupportCenter
import com.mtv.app.shopme.domain.model.SupportSellerTerm
import com.mtv.app.shopme.domain.usecase.GetSupportCenterUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeTncEffect
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeTncEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerCreateCafeTncViewModel
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
import org.junit.Rule
import org.junit.Test

class SellerCreateCafeTncViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getSupportCenterUseCase: GetSupportCenterUseCase = mockk()
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Test
    fun `load should populate onboarding terms from backend`() = runTest {
        every { getSupportCenterUseCase.invoke() } returns flowOf(Resource.Success(sampleSupportCenter()))

        val vm = SellerCreateCafeTncViewModel(
            getSupportCenterUseCase = getSupportCenterUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(SellerCreateCafeTncEvent.Load)
        advanceUntilIdle()

        assertEquals("Create Your Cafe", vm.uiState.value.title)
        assertEquals(3, vm.uiState.value.terms.size)
        assertEquals("Accurate Cafe Information", vm.uiState.value.terms.first().title)
    }

    @Test
    fun `next should navigate after all backend terms are checked`() = runTest {
        every { getSupportCenterUseCase.invoke() } returns flowOf(Resource.Success(sampleSupportCenter()))

        val vm = SellerCreateCafeTncViewModel(
            getSupportCenterUseCase = getSupportCenterUseCase,
            sessionManager = sessionManager
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(SellerCreateCafeTncEvent.Load)
        advanceUntilIdle()
        vm.uiState.value.terms.forEach { item ->
            vm.onEvent(SellerCreateCafeTncEvent.ToggleTerm(item.id, true))
        }
        vm.onEvent(SellerCreateCafeTncEvent.Next)
        advanceUntilIdle()

        assertEquals(SellerCreateCafeTncEffect.NavigateNext, effect.await())
    }

    private fun sampleSupportCenter() = SupportCenter(
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
        faq = emptyList(),
        bootstrapMessages = emptyList(),
        sellerTerms = listOf(
            SupportSellerTerm("term-1", "Accurate Cafe Information", "Provide accurate information."),
            SupportSellerTerm("term-2", "Food Safety and Quality Compliance", "Follow food safety rules."),
            SupportSellerTerm("term-3", "Valid and Accessible Cafe Location", "Provide a valid location.")
        )
    )
}
