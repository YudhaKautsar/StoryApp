package com.yudhakautsar.storyapp.domain.usecase.auth

import com.yudhakautsar.storyapp.base.Resource
import com.yudhakautsar.storyapp.domain.repository.AuthRepository
import com.yudhakautsar.storyapp.domain.usecase.UseCase

class RegisterUseCase(
    private val repository: AuthRepository
) : UseCase<RegisterUseCase.Params, Unit> {

    override suspend fun invoke(params: Params): Resource<Unit> {
        return repository.register(params.name, params.email, params.password)
    }

    data class Params(
        val name: String,
        val email: String,
        val password: String
    )
}
