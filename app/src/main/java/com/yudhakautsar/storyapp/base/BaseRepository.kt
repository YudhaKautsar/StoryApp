package com.yudhakautsar.storyapp.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseRepository {

    protected suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                Resource.Error(throwable.message ?: "Unknown error occurred")
            }
        }
    }

    protected suspend fun <T> safeApiCallWithErrorHandler(
        apiCall: suspend () -> T,
        errorHandler: (Throwable) -> String
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                Resource.Error(errorHandler(throwable))
            }
        }
    }
}

