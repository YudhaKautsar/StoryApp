package com.yudhakautsar.storyapp.ui.auth.login

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import com.yudhakautsar.storyapp.R
import com.yudhakautsar.storyapp.StoryApplication
import com.yudhakautsar.storyapp.base.BaseActivity
import com.yudhakautsar.storyapp.base.ViewState
import com.yudhakautsar.storyapp.databinding.ActivityLoginBinding
import com.yudhakautsar.storyapp.ui.auth.register.RegisterActivity
import com.yudhakautsar.storyapp.ui.story.list.StoryListActivity
import com.yudhakautsar.storyapp.utils.showToast

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels {
        val appContainer = (application as StoryApplication).appContainer
        LoginViewModelFactory(appContainer.loginUseCase, appContainer.userPreference)
    }

    override fun getViewBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun setupObservers() {
        viewModel.loginState.observe(this) { state ->
            when (state) {
                is ViewState.Idle -> hideLoading()
                is ViewState.Loading -> showLoading()
                is ViewState.Success -> {
                    hideLoading()
                    handleLoginSuccess()
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
            btnLogin.setOnClickListener {
                handleLogin()
            }

            tvRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
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

    private fun handleLogin() {
        binding.apply {
            val email = etLoginEmail.getEmail()
            val password = etLoginPassword.getPassword()

            when {
                email.isEmpty() || password.isEmpty() -> {
                    showError(getString(R.string.error_empty_fields))
                }
                !etLoginEmail.isValid() -> {
                    showError(getString(R.string.error_invalid_email))
                }
                !etLoginPassword.isValid() -> {
                    showError(getString(R.string.error_invalid_password))
                }
                else -> {
                    viewModel.login(email, password)
                }
            }
        }
    }

    private fun handleLoginSuccess() {
        showToast(getString(R.string.login_success))
        val intent = Intent(this, StoryListActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun showLoading() {
        binding.apply {
            btnLogin.setLoading(true)
            etLoginEmail.isEnabled = false
            etLoginPassword.isEnabled = false
        }
    }

    override fun hideLoading() {
        binding.apply {
            btnLogin.setLoading(false)
            etLoginEmail.isEnabled = true
            etLoginPassword.isEnabled = true
        }
    }

    override fun showError(message: String) {
        showToast(message)
    }
}
