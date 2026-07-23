package com.example.core_common

sealed interface AppResult<out T, out E> {

    data class Success<T>(
        val data: T
    ) : AppResult<T, Nothing>

    data class Error<E>(
        val error: E
    ) : AppResult<Nothing, E>
}