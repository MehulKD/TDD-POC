package com.example.domain.error

sealed interface LoginError {

    data object InvalidCredentials : LoginError

    data object EmptyUsername : LoginError

    data object EmptyPassword : LoginError

    data object NetworkError : LoginError

    data object Unknown : LoginError
}

fun LoginError.toMessage(): String = when (this) {
    LoginError.InvalidCredentials -> "Invalid credentials"
    LoginError.EmptyUsername -> "Username cannot be empty"
    LoginError.EmptyPassword -> "Password cannot be empty"
    LoginError.NetworkError -> "Network error"
    LoginError.Unknown -> "Something went wrong"
}