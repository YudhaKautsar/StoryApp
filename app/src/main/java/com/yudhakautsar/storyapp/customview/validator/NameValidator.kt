package com.yudhakautsar.storyapp.customview.validator

class NameValidator(
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
