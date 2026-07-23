package com.example.feature.login.presentation

sealed interface LoginUiAction {

    data class UsernameChanged(val username: String) : LoginUiAction

    data class PasswordChanged(val password: String) : LoginUiAction

    data object LoginClicked : LoginUiAction
}