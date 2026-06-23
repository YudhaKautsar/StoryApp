package com.yudhakautsar.storyapp.data.remote.api

import com.yudhakautsar.storyapp.data.remote.dto.LoginRequest
import com.yudhakautsar.storyapp.data.remote.dto.LoginResponse
import com.yudhakautsar.storyapp.data.remote.dto.RegisterRequest
import com.yudhakautsar.storyapp.data.remote.dto.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    
    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse
}
