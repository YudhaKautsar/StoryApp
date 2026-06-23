package com.yudhakautsar.storyapp.data.mapper

import com.yudhakautsar.storyapp.data.remote.dto.LoginResponse
import com.yudhakautsar.storyapp.domain.model.User

object UserMapper {
    
    fun toDomain(response: LoginResponse): User {
        return User(
            userId = response.loginResult?.userId.orEmpty(),
            name = response.loginResult?.name.orEmpty(),
            token = response.loginResult?.token.orEmpty()
        )
    }
}

