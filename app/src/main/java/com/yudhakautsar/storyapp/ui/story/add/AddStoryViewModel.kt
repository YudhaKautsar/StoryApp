package com.yudhakautsar.storyapp.ui.story.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yudhakautsar.storyapp.base.BaseViewModel
import com.yudhakautsar.storyapp.base.ViewState
import com.yudhakautsar.storyapp.data.local.UserPreference
import com.yudhakautsar.storyapp.domain.usecase.story.AddStoryUseCase
import kotlinx.coroutines.flow.first
import java.io.File

class AddStoryViewModel(
    private val addStoryUseCase: AddStoryUseCase,
    private val userPreference: UserPreference
) : BaseViewModel() {

    private val _uploadState = MutableLiveData<ViewState<String>>()
    val uploadState: LiveData<ViewState<String>> = _uploadState

    fun uploadStory(file: File, description: String) {
        _uploadState.value = ViewState.Loading
        launchWithExceptionHandler {
            val token = userPreference.getToken().first()
            if (token.isNotEmpty()) {
                val result = addStoryUseCase(token, file, description)
                result.onSuccess {
                    _uploadState.postValue(ViewState.Success(it))
                }.onFailure {
                    _uploadState.postValue(ViewState.Error(it.message ?: "Upload failed"))
                }
            } else {
                _uploadState.postValue(ViewState.Error("Session expired"))
            }
        }
    }

    override fun onError(throwable: Throwable) {
        super.onError(throwable)
        _uploadState.postValue(ViewState.Error(throwable.message ?: "Unknown error occurred"))
    }
}
