package com.yudhakautsar.storyapp.domain.exception

import androidx.annotation.StringRes
import com.yudhakautsar.storyapp.R

sealed class AppException(
    @StringRes val messageResId: Int,
    val messageString: String? = null
) : Exception(messageString) {

    class NetworkException : AppException(R.string.error_network)

    class TimeoutException : AppException(R.string.error_timeout)

    class ValidationException(message: String?) : AppException(
        messageResId = 0,
        messageString = message
    )

    class UnknownException(message: String? = null) : AppException(
        messageResId = R.string.error_unknown,
        messageString = message
    )
}

