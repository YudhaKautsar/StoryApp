package com.yudhakautsar.storyapp.domain.usecase.auth

import com.yudhakautsar.storyapp.base.Resource
import com.yudhakautsar.storyapp.domain.model.User
import com.yudhakautsar.storyapp.domain.repository.AuthRepository
import com.yudhakautsar.storyapp.domain.usecase.UseCase

class LoginUseCase(
    private val repository: AuthRepository
) : UseCase<LoginUseCase.Params, User> {

    override suspend fun invoke(params: Params): Resource<User> {
        return repository.login(params.email, params.password)
    }

    data class Params(
        val email: String,
        val password: String
    )
}

