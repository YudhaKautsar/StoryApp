package com.yudhakautsar.storyapp.ui.auth.register

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import androidx.activity.viewModels
import com.yudhakautsar.storyapp.R
import com.yudhakautsar.storyapp.StoryApplication
import com.yudhakautsar.storyapp.base.BaseActivity
import com.yudhakautsar.storyapp.base.ViewState
import com.yudhakautsar.storyapp.databinding.ActivityRegisterBinding
import com.yudhakautsar.storyapp.utils.showToast

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    private val viewModel: RegisterViewModel by viewModels {
        val appContainer = (application as StoryApplication).appContainer
        RegisterViewModelFactory(appContainer.registerUseCase)
    }

    override fun getViewBinding(): ActivityRegisterBinding {
        return ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun setupObservers() {
        viewModel.registerState.observe(this) { state ->
            when (state) {
                is ViewState.Idle -> hideLoading()
                is ViewState.Loading -> showLoading()
                is ViewState.Success -> {
                    hideLoading()
                    handleRegisterSuccess(state.data)
                }
                is ViewState.Error -> {
                    hideLoading()
                    showError(state.message)
                }
                else -> hideLoading()
            }
        }
    }

    override fun setupViews() {
        supportActionBar?.hide()
        playAnimation()
    }

    override fun setupListeners() {
        binding.apply {
            btnRegister.setOnClickListener {
                handleRegister()
            }

            tvLogin.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun playAnimation() {
        binding.apply {
            ObjectAnimator.ofFloat(ivLogo, View.TRANSLATION_X, -30f, 30f).apply {
                duration = 6000
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
            }.start()
        }
    }

    private fun handleRegister() {
        binding.apply {
            val name = etRegisterName.text.toString().trim()
            val email = etRegisterEmail.getEmail()
            val password = etRegisterPassword.getPassword()

            when {
                name.isEmpty() || email.isEmpty() || password.isEmpty() -> {
                    showError(getString(R.string.error_empty_fields))
                }
                !etRegisterEmail.isValid() -> {
                    showError(getString(R.string.error_invalid_email))
                }
                !etRegisterPassword.isValid() -> {
                    showError(getString(R.string.error_invalid_password))
                }
                else -> {
                    viewModel.register(name, email, password)
                }
            }
        }
    }

    private fun handleRegisterSuccess(message: String) {
        showToast(message)
        finish()
    }

    override fun showLoading() {
        binding.apply {
            btnRegister.setLoading(true)
            etRegisterName.isEnabled = false
            etRegisterEmail.isEnabled = false
            etRegisterPassword.isEnabled = false
        }
    }

    override fun hideLoading() {
        binding.apply {
            btnRegister.setLoading(false)
            etRegisterName.isEnabled = true
            etRegisterEmail.isEnabled = true
            etRegisterPassword.isEnabled = true
        }
    }

    override fun showError(message: String) {
        showToast(message)
    }
}
