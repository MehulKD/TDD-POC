package com.example.feature.login.presentation

import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.feature.login.test.LoginTags

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onAction: (LoginUiAction) -> Unit,
    onNavigate: () -> Unit
) {

    OutlinedTextField(

        value = uiState.username,

        onValueChange = {},

        modifier = Modifier.testTag(LoginTags.USERNAME)

    )

}