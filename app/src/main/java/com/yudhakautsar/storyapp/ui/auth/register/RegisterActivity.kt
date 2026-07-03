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
                is ViewState.Idle -> {
                    hideLoading()
                }
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
                    handleRegisterSuccess(state.data)
                }
                is ViewState.Error -> {
                    hideLoading()
                    showError(state.message)
                }
                is ViewState.Empty -> {
                    hideLoading()
                }
            }
        }
    }

    override fun setupViews() {
        supportActionBar?.hide()
        playAnimation()
    }

    override fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            handleRegister()
        }

        binding.tvLogin.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
        }.start()
    }

    private fun handleRegister() {
        val name = binding.etRegisterName.text.toString().trim()
        val email = binding.etRegisterEmail.getEmail()
        val password = binding.etRegisterPassword.getPassword()

        when {
            name.isEmpty() || email.isEmpty() || password.isEmpty() -> {
                showError(getString(R.string.error_empty_fields))
            }
            !binding.etRegisterEmail.isValid() -> {
                showError(getString(R.string.error_invalid_email))
            }
            !binding.etRegisterPassword.isValid() -> {
                showError(getString(R.string.error_invalid_password))
            }
            else -> {
                viewModel.register(name, email, password)
            }
        }
    }

    private fun handleRegisterSuccess(message: String) {
        showToast(message)
        finish()
    }

    override fun showLoading() {
        binding.btnRegister.setLoading(true)
        binding.etRegisterName.isEnabled = false
        binding.etRegisterEmail.isEnabled = false
        binding.etRegisterPassword.isEnabled = false
    }

    override fun hideLoading() {
        binding.btnRegister.setLoading(false)
        binding.etRegisterName.isEnabled = true
        binding.etRegisterEmail.isEnabled = true
        binding.etRegisterPassword.isEnabled = true
    }

    override fun showError(message: String) {
        showToast(message)
    }
}
