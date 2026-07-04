package com.yudhakautsar.storyapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.yudhakautsar.storyapp.StoryApplication
import com.yudhakautsar.storyapp.base.BaseActivity
import com.yudhakautsar.storyapp.databinding.ActivitySplashBinding
import com.yudhakautsar.storyapp.ui.auth.login.LoginActivity
import com.yudhakautsar.storyapp.ui.story.list.StoryListActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private val viewModel: SplashViewModel by viewModels {
        SplashViewModelFactory((application as StoryApplication).appContainer.userPreference)
    }

    override fun getViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        setupFullscreen()
        playAnimation()
    }

    private fun setupFullscreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun playAnimation() {
        binding.apply {
            ivLogo.alpha = 0f
            ivLogo.animate()
                .alpha(1f)
                .setDuration(SPLASH_DURATION)
                .withEndAction {
                    viewModel.checkLoginStatus()
                }
                .start()
        }
    }

    override fun setupObservers() {
        viewModel.isLoggedIn.observe(this) { isLoggedin ->
            val intent = if (isLoggedin) {
                Intent(this, StoryListActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
    }

    companion object {
        private const val SPLASH_DURATION = 1500L
    }
}
