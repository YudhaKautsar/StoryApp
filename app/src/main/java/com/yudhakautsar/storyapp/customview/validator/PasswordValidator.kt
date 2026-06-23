package com.yudhakautsar.storyapp.customview.validator

import android.content.Context
import com.yudhakautsar.storyapp.R

class PasswordValidator(
    private val context: Context,
    private val minLength: Int = DEFAULT_MIN_LENGTH
) : InputValidator {

    override fun validate(input: String): ValidationResult {
        return when {
            input.isEmpty() -> ValidationResult(
                isValid = false,
                errorMessage = null
            )
            input.length < minLength -> ValidationResult(
                isValid = false,
                errorMessage = context.getString(R.string.validation_password_min_length, minLength)
            )
            else -> ValidationResult(isValid = true)
        }
    }

    companion object {
        const val DEFAULT_MIN_LENGTH = 8
    }
}

