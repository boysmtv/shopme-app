package com.mtv.app.shopme.feature.customer

import android.content.Intent
import com.mtv.app.shopme.feature.customer.contract.SupportEffect
import com.mtv.app.shopme.feature.customer.contract.SupportEvent
import com.mtv.app.shopme.feature.customer.presentation.SupportViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
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

    @Test
    fun `open whatsapp should emit real whatsapp intent`() = runTest {
        val vm = SupportViewModel()
        val effect = async { vm.effect.first() as SupportEffect.OpenIntent }

        vm.onEvent(SupportEvent.OpenWhatsapp)
        advanceUntilIdle()

        val result = effect.await().intent
        assertEquals(Intent.ACTION_VIEW, result.action)
        assertTrue(result.data.toString().startsWith("https://wa.me/"))
    }

    @Test
    fun `open email should emit mailto intent`() = runTest {
        val vm = SupportViewModel()
        val effect = async { vm.effect.first() as SupportEffect.OpenIntent }

        vm.onEvent(SupportEvent.OpenEmail)
        advanceUntilIdle()

        val result = effect.await().intent
        assertEquals(Intent.ACTION_SENDTO, result.action)
        assertTrue(result.data.toString().startsWith("mailto:"))
    }
}
