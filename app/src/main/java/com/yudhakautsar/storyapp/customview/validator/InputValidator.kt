package com.yudhakautsar.storyapp.customview.validator

interface InputValidator {
    fun validate(input: String): ValidationResult
}

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

