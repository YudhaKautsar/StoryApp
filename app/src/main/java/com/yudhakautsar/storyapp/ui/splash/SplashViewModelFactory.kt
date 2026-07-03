package com.yudhakautsar.storyapp.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yudhakautsar.storyapp.data.local.UserPreference

class SplashViewModelFactory(private val pref: UserPreference) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SplashViewModel(pref) as T
    }
}