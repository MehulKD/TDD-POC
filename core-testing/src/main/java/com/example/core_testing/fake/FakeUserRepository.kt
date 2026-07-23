package com.example.core_testing.fake

import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import com.example.core_common.AppResult
import com.example.domain.error.LoginError
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class FakeUserRepository : UserRepository {
    var loginCalled = false
    override suspend fun login(
        username: String,
        password: String
    ): AppResult<User, LoginError> {
        println("Repository started")
        loginCalled = true
        val result= if (username.isBlank()) {
            AppResult.Error(LoginError.EmptyUsername)
        } else if (password.isBlank()) {
            AppResult.Error(LoginError.EmptyPassword)
        } else if (username == "admin" && password == "1234") {
            AppResult.Success(User(1, "Administrator"))
        } else {
            AppResult.Error(LoginError.InvalidCredentials)
        }
        println("Repository finished")
        return result
    }
}