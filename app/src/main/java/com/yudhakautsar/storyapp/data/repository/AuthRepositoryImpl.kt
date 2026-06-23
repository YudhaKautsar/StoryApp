package com.yudhakautsar.storyapp.data.repository

import com.yudhakautsar.storyapp.base.BaseRepository
import com.yudhakautsar.storyapp.base.Resource
import com.yudhakautsar.storyapp.data.mapper.UserMapper
import com.yudhakautsar.storyapp.data.remote.api.AuthApiService
import com.yudhakautsar.storyapp.data.remote.dto.LoginRequest
import com.yudhakautsar.storyapp.data.remote.dto.RegisterRequest
import com.yudhakautsar.storyapp.domain.exception.AppException
import com.yudhakautsar.storyapp.domain.model.User
import com.yudhakautsar.storyapp.domain.repository.AuthRepository
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AuthRepositoryImpl(
    private val apiService: AuthApiService
) : BaseRepository(), AuthRepository {

    override suspend fun register(name: String, email: String, password: String): Resource<Unit> {
        return safeApiCallWithErrorHandler(
            apiCall = {
                val request = RegisterRequest(name, email, password)
                val response = apiService.register(request)

                if (response.error) {
                    throw AppException.ValidationException(response.message)
                }
            },
            errorHandler = ::handleError
        )
    }

    override suspend fun login(email: String, password: String): Resource<User> {
        return safeApiCallWithErrorHandler(
            apiCall = {
                val request = LoginRequest(email, password)
                val response = apiService.login(request)

                if (response.error) {
                    throw AppException.ValidationException(response.message)
                }

                UserMapper.toDomain(response)
            },
            errorHandler = ::handleError
        )
    }

    override suspend fun logout(): Resource<Unit> {
        return Resource.Success(Unit)
    }

    override suspend fun isLoggedIn(): Boolean {
        return false
    }

    override suspend fun getToken(): String? {
        return null
    }

    private fun handleError(throwable: Throwable): String {
        val exception = when (throwable) {
            is UnknownHostException -> AppException.NetworkException()
            is SocketTimeoutException -> AppException.TimeoutException()
            is AppException -> throwable
            else -> AppException.UnknownException(throwable.message)
        }
        return exception.messageString ?: exception.message ?: ""
    }
}
