package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import com.example.core_common.AppResult
import com.example.domain.error.LoginError

class LoginUseCase(

    private val repository: UserRepository

) {

    suspend operator fun invoke(

        username: String,

        password: String

    ): AppResult<User, LoginError> {

        return repository.login(username = username.trim().lowercase(),  password.trim())

    }

}