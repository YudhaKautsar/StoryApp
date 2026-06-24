package com.yudhakautsar.storyapp.data.repository

import com.google.gson.Gson
import com.yudhakautsar.storyapp.base.BaseRepository
import com.yudhakautsar.storyapp.base.Resource
import com.yudhakautsar.storyapp.data.mapper.UserMapper
import com.yudhakautsar.storyapp.data.remote.api.AuthApiService
import com.yudhakautsar.storyapp.data.remote.dto.LoginRequest
import com.yudhakautsar.storyapp.data.remote.dto.RegisterRequest
import com.yudhakautsar.storyapp.data.remote.dto.RegisterResponse
import com.yudhakautsar.storyapp.domain.exception.AppException
import com.yudhakautsar.storyapp.domain.model.User
import com.yudhakautsar.storyapp.domain.repository.AuthRepository
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AuthRepositoryImpl(
    private val apiService: AuthApiService
) : BaseRepository(), AuthRepository {

    override suspend fun register(name: String, email: String, password: String): Resource<String> {
        return safeApiCallWithErrorHandler(
            apiCall = {
                val request = RegisterRequest(name, email, password)
                val response = apiService.register(request)

                if (response.error) {
                    throw AppException.ValidationException(response.message)
                }
                response.message
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
            is HttpException -> {
                val errorBody = throwable.response()?.errorBody()?.string()

                val errorMessage = try {
                    val response = Gson().fromJson(errorBody, RegisterResponse::class.java)
                    response.message
                } catch (e: Exception) {
                    e.message
                }
                AppException.ValidationException(errorMessage)
            }
            else -> AppException.UnknownException(throwable.message)
        }
        return exception.messageString ?: exception.message ?: ""
    }
}
