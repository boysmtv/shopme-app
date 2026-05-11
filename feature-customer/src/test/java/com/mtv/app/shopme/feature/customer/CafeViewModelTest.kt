package com.mtv.app.shopme.feature.customer

import androidx.lifecycle.SavedStateHandle
import com.mtv.app.shopme.domain.model.Cafe
import com.mtv.app.shopme.domain.model.CafeAddress
import com.mtv.app.shopme.domain.usecase.EnsureChatConversationUseCase
import com.mtv.app.shopme.domain.usecase.GetCafeUseCase
import com.mtv.app.shopme.domain.usecase.GetFoodsByCafeUseCase
import com.mtv.app.shopme.feature.customer.contract.CafeEffect
import com.mtv.app.shopme.feature.customer.contract.CafeEvent
import com.mtv.app.shopme.feature.customer.presentation.CafeViewModel
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.provider.utils.SessionManager
import io.mockk.every
import io.mockk.mockk
import java.math.BigDecimal
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CafeViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getCafeUseCase: GetCafeUseCase = mockk()
    private val getFoodsByCafeUseCase: GetFoodsByCafeUseCase = mockk()
    private val ensureChatConversationUseCase: EnsureChatConversationUseCase = mockk()
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Test
    fun `click whatsapp should emit backend powered whatsapp effect`() = runTest {
        every { getCafeUseCase.invoke("cafe-1") } returns flowOf(
            Resource.Success(
                Cafe(
                    id = "cafe-1",
                    customerId = "seller-1",
                    name = "Kopi Kita",
                    phone = "+62 812-3456-7890",
                    description = "Cafe enak",
                    minimalOrder = BigDecimal("15000"),
                    openTime = "08:00",
                    closeTime = "22:00",
                    image = "",
                    isActive = true,
                    createdAt = "2026-05-11T08:00:00Z",
                    address = CafeAddress(
                        id = "addr-1",
                        name = "Kemang",
                        block = "A",
                        number = "12",
                        rt = "01",
                        rw = "02"
                    )
                )
            )
        )
        every { getFoodsByCafeUseCase.invoke("cafe-1") } returns flowOf(Resource.Success(emptyList()))

        val vm = CafeViewModel(
            getCafeUseCase = getCafeUseCase,
            getFoodsByCafeUseCase = getFoodsByCafeUseCase,
            ensureChatConversationUseCase = ensureChatConversationUseCase,
            sessionManager = sessionManager,
            savedStateHandle = SavedStateHandle(mapOf("cafeId" to "cafe-1"))
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(CafeEvent.Load)
        advanceUntilIdle()
        vm.onEvent(CafeEvent.ClickWhatsapp)

        assertEquals(CafeEffect.OpenWhatsapp("6281234567890"), effect.await())
    }

    @Test
    fun `click search should navigate to customer search`() = runTest {
        val vm = CafeViewModel(
            getCafeUseCase = getCafeUseCase,
            getFoodsByCafeUseCase = getFoodsByCafeUseCase,
            ensureChatConversationUseCase = ensureChatConversationUseCase,
            sessionManager = sessionManager,
            savedStateHandle = SavedStateHandle(mapOf("cafeId" to "cafe-1"))
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(CafeEvent.ClickSearch)

        assertEquals(CafeEffect.NavigateToSearch, effect.await())
    }

    @Test
    fun `click chat should ensure scoped conversation then navigate to that chat`() = runTest {
        every { ensureChatConversationUseCase.invoke("cafe-1") } returns flowOf(Resource.Success("conv-1"))

        val vm = CafeViewModel(
            getCafeUseCase = getCafeUseCase,
            getFoodsByCafeUseCase = getFoodsByCafeUseCase,
            ensureChatConversationUseCase = ensureChatConversationUseCase,
            sessionManager = sessionManager,
            savedStateHandle = SavedStateHandle(mapOf("cafeId" to "cafe-1"))
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(CafeEvent.ClickChat)
        advanceUntilIdle()

        assertEquals(CafeEffect.NavigateToChat("conv-1"), effect.await())
    }
}
