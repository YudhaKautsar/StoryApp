package com.yudhakautsar.storyapp.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yudhakautsar.storyapp.base.BaseViewModel
import com.yudhakautsar.storyapp.base.Resource
import com.yudhakautsar.storyapp.base.ViewState
import com.yudhakautsar.storyapp.domain.usecase.auth.RegisterUseCase

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : BaseViewModel() {

    private val _registerState = MutableLiveData<ViewState<String>>()
    val registerState: LiveData<ViewState<String>> = _registerState

    init {
        _registerState.value = ViewState.Idle
    }

    fun register(name: String, email: String, password: String) {
        _registerState.value = ViewState.Loading

        launchWithExceptionHandler {
            val params = RegisterUseCase.Params(name, email, password)
            when (val result = registerUseCase(params)) {
                is Resource.Success -> {
                    _registerState.postValue(ViewState.Success(result.data))
                }
                is Resource.Error -> {
                    _registerState.postValue(ViewState.Error(result.message))
                }
                is Resource.Loading -> {
                    _registerState.postValue(ViewState.Loading)
                }
                else -> {}
            }
        }
    }

    override fun onError(throwable: Throwable) {
        super.onError(throwable)
        _registerState.postValue(ViewState.Error(throwable.message ?: "", throwable))
    }
}
