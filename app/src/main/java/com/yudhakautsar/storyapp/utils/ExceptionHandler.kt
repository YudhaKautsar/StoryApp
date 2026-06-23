package com.yudhakautsar.storyapp.utils

import android.content.Context
import com.yudhakautsar.storyapp.R
import com.yudhakautsar.storyapp.domain.exception.AppException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ExceptionHandler {
    
    fun getErrorMessage(context: Context, throwable: Throwable): String {
        return when (throwable) {
            is AppException -> {
                if (throwable.messageResId != 0) {
                    context.getString(throwable.messageResId)
                } else {
                    throwable.messageString ?: context.getString(R.string.error_unknown)
                }
            }
            is UnknownHostException -> context.getString(R.string.error_network)
            is SocketTimeoutException -> context.getString(R.string.error_timeout)
            else -> throwable.message ?: context.getString(R.string.error_general)
        }
    }
}

