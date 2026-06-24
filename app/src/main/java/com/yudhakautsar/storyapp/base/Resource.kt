package com.yudhakautsar.storyapp.base

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class SuccessMessage(val message: String) : Resource<Nothing>()

    data class Error(val message: String) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}

