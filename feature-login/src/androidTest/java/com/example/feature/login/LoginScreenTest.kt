package com.example.feature.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.feature.login.presentation.LoginScreen
import com.example.feature.login.presentation.LoginUiState
import com.example.feature.login.test.LoginTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun username_textfield_is_displayed() {

        composeRule.setContent {

            LoginScreen(
                uiState = LoginUiState(),
                onAction = {},
                onNavigate = {})
        }

        composeRule
            .onNodeWithTag(LoginTags.USERNAME)
            .assertExists()
            .assertIsDisplayed()
    }
}