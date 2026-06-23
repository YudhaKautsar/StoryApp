package com.yudhakautsar.storyapp.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout
import com.yudhakautsar.storyapp.customview.validator.EmailValidator
import com.yudhakautsar.storyapp.customview.validator.InputValidator

class EmailEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private val validator: InputValidator = EmailValidator(context)

    init {
        setupView()
    }

    private fun setupView() {
        addTextChangedListener(EmailTextWatcher())
    }

    private inner class EmailTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            performValidation(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun performValidation(input: String) {
        val result = validator.validate(input)
        updateErrorState(result.errorMessage)
    }

    private fun updateErrorState(errorMessage: String?) {
        getTextInputLayout()?.error = errorMessage
    }

    private fun getTextInputLayout(): TextInputLayout? {
        return parent.parent as? TextInputLayout
    }

    fun isValid(): Boolean {
        return validator.validate(text.toString()).isValid
    }

    fun getEmail(): String {
        return text.toString()
    }
}
