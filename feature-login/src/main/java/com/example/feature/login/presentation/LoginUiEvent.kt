package com.example.feature.login.presentation

import com.example.domain.error.LoginError

sealed interface LoginUiEvent {

    data object NavigateToHome : LoginUiEvent

    data class ShowSnackbar(
        val error: LoginError
    ) : LoginUiEvent
}