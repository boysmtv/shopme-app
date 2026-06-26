package com.mtv.app.shopme.feature.auth.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.mtv.app.shopme.feature.auth.contract.LoginEvent
import com.mtv.app.shopme.feature.auth.contract.LoginUiState
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun login_screen_shows_title() {
        composeTestRule.setContent {
            LoginScreen(
                state = LoginUiState(),
                event = {}
            )
        }
        composeTestRule.onNodeWithText("Shopme").assertExists()
    }

    @Test
    fun login_screen_shows_login_button() {
        composeTestRule.setContent {
            LoginScreen(
                state = LoginUiState(),
                event = {}
            )
        }
        composeTestRule.onNodeWithText("Masuk").assertExists()
    }

    @Test
    fun login_screen_shows_register_link() {
        composeTestRule.setContent {
            LoginScreen(
                state = LoginUiState(),
                event = {}
            )
        }
        composeTestRule.onNodeWithText("Register").assertExists()
    }
}
