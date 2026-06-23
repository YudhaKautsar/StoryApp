package com.yudhakautsar.storyapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import com.yudhakautsar.storyapp.base.BaseActivity
import com.yudhakautsar.storyapp.databinding.ActivitySplashBinding
import com.yudhakautsar.storyapp.ui.auth.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

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
        binding.ivLogo.alpha = 0f
        binding.ivLogo.animate()
            .alpha(1f)
            .setDuration(SPLASH_DURATION)
            .withEndAction {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }, DELAY_BEFORE_NEXT)
            }
            .start()
    }

    companion object {
        private const val SPLASH_DURATION = 1500L
        private const val DELAY_BEFORE_NEXT = 500L
    }
}
