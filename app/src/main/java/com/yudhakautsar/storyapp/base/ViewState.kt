package com.yudhakautsar.storyapp.base

sealed class ViewState<out T> {
    object Idle : ViewState<Nothing>()
    object Loading : ViewState<Nothing>()
    data class Success<out T>(val data: T) : ViewState<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : ViewState<Nothing>()
    object Empty : ViewState<Nothing>()
}

