package com.example.core.testing.fake

import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import com.example.core_common.AppResult
import com.example.domain.error.LoginError

class FakeUserRepository : UserRepository {
    var lastUsername: String? = null
    var lastPassword: String? = null
    var loginCalled = false
    private var result: AppResult<User, LoginError> =
        AppResult.Success(User(1, "Administrator"))

    fun givenLoginSuccess(
        user: User = User(1, "Administrator")
    ) {
        result = AppResult.Success(user)
    }

    fun givenLoginFailure(
        error: LoginError
    ) {
        result = AppResult.Error(error)
    }
    override suspend fun login(
        username: String,
        password: String
    ): AppResult<User, LoginError> {
        lastUsername = username
        lastPassword = password
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
        return result
    }
}