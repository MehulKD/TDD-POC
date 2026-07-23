package com.example.domain.repository

import com.example.domain.model.User
import com.example.core_common.AppResult
import com.example.domain.error.LoginError

interface UserRepository {

    suspend fun login(

        username: String,

        password: String

    ): AppResult<User, LoginError>

}