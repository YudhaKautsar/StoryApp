package com.yudhakautsar.storyapp.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yudhakautsar.storyapp.base.BaseViewModel
import com.yudhakautsar.storyapp.base.Resource
import com.yudhakautsar.storyapp.base.ViewState
import com.yudhakautsar.storyapp.data.local.UserPreference
import com.yudhakautsar.storyapp.domain.model.User
import com.yudhakautsar.storyapp.domain.usecase.auth.LoginUseCase

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val userPreference: UserPreference
) : BaseViewModel() {

    private val _loginState = MutableLiveData<ViewState<User>>()
    val loginState: LiveData<ViewState<User>> = _loginState

    init {
        _loginState.value = ViewState.Idle
    }

    fun login(email: String, password: String) {
        _loginState.value = ViewState.Loading

        launchWithExceptionHandler {
            val params = LoginUseCase.Params(email, password)
            when (val result = loginUseCase(params)) {
                is Resource.Success -> {
                    userPreference.saveToken(result.data.token)
                    _loginState.postValue(ViewState.Success(result.data))
                }
                is Resource.Error -> {
                    _loginState.postValue(ViewState.Error(result.message))
                }
                is Resource.Loading -> {
                    _loginState.postValue(ViewState.Loading)
                }

                else -> {
                    _loginState.postValue(ViewState.Empty)
                }
            }
        }
    }

    override fun onError(throwable: Throwable) {
        super.onError(throwable)
        _loginState.postValue(ViewState.Error(throwable.message ?: "", throwable))
    }
}
