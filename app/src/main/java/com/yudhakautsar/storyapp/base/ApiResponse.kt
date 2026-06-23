package com.yudhakautsar.storyapp.base

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("error")
    val error: Boolean? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: T? = null
)

fun <T> ApiResponse<T>.isSuccess(): Boolean {
    return error == false
}

fun <T> ApiResponse<T>.getDataOrThrow(): T {
    return if (isSuccess() && data != null) {
        data
    } else {
        throw Exception(message ?: "Unknown error occurred")
    }
}

