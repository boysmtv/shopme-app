package com.mtv.app.shopme.feature.customer

import android.content.Intent
import com.mtv.app.shopme.feature.customer.contract.ChatSupportEffect
import com.mtv.app.shopme.feature.customer.contract.ChatSupportEvent
import com.mtv.app.shopme.feature.customer.presentation.ChatSupportViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ChatSupportViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    @Test
    fun `send message should emit whatsapp intent with encoded text`() = runTest {
        val vm = ChatSupportViewModel()
        val effect = async { vm.effect.first() as ChatSupportEffect.OpenIntent }

        vm.onEvent(ChatSupportEvent.OnMessageChange("butuh bantuan order"))
        vm.onEvent(ChatSupportEvent.SendMessage)
        advanceUntilIdle()

        val result = effect.await().intent
        assertEquals(Intent.ACTION_VIEW, result.action)
        assertTrue(result.data.toString().contains("https://wa.me/"))
        assertEquals("", vm.uiState.value.currentMessage)
    }
}
