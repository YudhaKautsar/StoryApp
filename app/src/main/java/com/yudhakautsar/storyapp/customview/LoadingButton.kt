package com.yudhakautsar.storyapp.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.yudhakautsar.storyapp.databinding.BtnLoadingBinding

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: BtnLoadingBinding = BtnLoadingBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private var originalText: String? = null

    init {
        context.theme.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.text), 0, 0).apply {
            try {
                val text = getString(0)
                setText(text ?: "")
            } finally {
                recycle()
            }
        }
    }

    fun setText(text: String) {
        binding.btnAction.text = text
        originalText = text
    }

    fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            originalText = binding.btnAction.text.toString()
            binding.btnAction.text = ""
            binding.btnAction.isEnabled = false
            binding.progressBar.visibility = VISIBLE
        } else {
            binding.btnAction.text = originalText
            binding.btnAction.isEnabled = true
            binding.progressBar.visibility = GONE
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.btnAction.setOnClickListener(l)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.btnAction.isEnabled = enabled
    }
}
