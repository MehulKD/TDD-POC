package com.example.domain.error

sealed interface LoginError {

    data object InvalidCredentials : LoginError

    data object EmptyUsername : LoginError

    data object EmptyPassword : LoginError

    data object NetworkError : LoginError

    data object Unknown : LoginError
}