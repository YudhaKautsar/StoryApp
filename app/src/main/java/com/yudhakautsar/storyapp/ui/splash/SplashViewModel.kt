package com.yudhakautsar.storyapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yudhakautsar.storyapp.data.local.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashViewModel(private val userPreference: UserPreference) : ViewModel() {
    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    fun checkLoginStatus() {
        viewModelScope.launch {
            val token = userPreference.getToken().first()
            _isLoggedIn.postValue(token.isNotEmpty())
        }
    }

}