package com.example.feature_login.presentation

sealed interface LoginUiEvent {

    data object NavigateToHome : LoginUiEvent

    data class ShowSnackbar(
        val message: String
    ) : LoginUiEvent
}