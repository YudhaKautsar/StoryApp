package com.yudhakautsar.storyapp.customview.validator

import android.content.Context
import android.util.Patterns
import com.yudhakautsar.storyapp.R

class EmailValidator(
    private val context: Context
) : InputValidator {

    override fun validate(input: String): ValidationResult {
        return when {
            input.isEmpty() -> ValidationResult(
                isValid = false,
                errorMessage = null
            )
            !isValidEmailFormat(input) -> ValidationResult(
                isValid = false,
                errorMessage = context.getString(R.string.error_invalid_email)
            )
            else -> ValidationResult(isValid = true)
        }
    }

    private fun isValidEmailFormat(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

