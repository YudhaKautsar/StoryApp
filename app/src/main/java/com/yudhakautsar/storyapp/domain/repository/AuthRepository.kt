package com.yudhakautsar.storyapp.domain.repository

import com.yudhakautsar.storyapp.base.Resource
import com.yudhakautsar.storyapp.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Resource<User>
    suspend fun register(name: String, email: String, password: String): Resource<Unit>
    suspend fun logout(): Resource<Unit>
    suspend fun isLoggedIn(): Boolean
    suspend fun getToken(): String?
}
