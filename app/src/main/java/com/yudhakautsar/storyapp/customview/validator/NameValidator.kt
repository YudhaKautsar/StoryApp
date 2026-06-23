package com.yudhakautsar.storyapp.customview.validator

import android.content.Context
import com.yudhakautsar.storyapp.R

class NameValidator(
    private val context: Context
) : InputValidator {

    override fun validate(input: String): ValidationResult {
        return when {
            input.isEmpty() -> ValidationResult(
                isValid = false,
                errorMessage = null
            )
            else -> ValidationResult(isValid = true)
        }
    }
}
