package com.yudhakautsar.storyapp.ui.story.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yudhakautsar.storyapp.base.BaseViewModel
import com.yudhakautsar.storyapp.base.ViewState
import com.yudhakautsar.storyapp.data.local.UserPreference
import com.yudhakautsar.storyapp.domain.model.Story
import com.yudhakautsar.storyapp.domain.usecase.story.GetStoryDetailUseCase
import kotlinx.coroutines.flow.first

class StoryDetailViewModel(
    private val getStoryDetailUseCase: GetStoryDetailUseCase,
    private val userPreference: UserPreference
) : BaseViewModel() {

    private val _storyDetailState = MutableLiveData<ViewState<Story>>()
    val storyDetailState: LiveData<ViewState<Story>> = _storyDetailState

    fun getStoryDetail(id: String) {
        _storyDetailState.value = ViewState.Loading
        launchWithExceptionHandler {
            val token = userPreference.getToken().first()
            if (token.isNotEmpty()) {
                val result = getStoryDetailUseCase(token, id)
                result.onSuccess {
                    _storyDetailState.postValue(ViewState.Success(it))
                }.onFailure {
                    _storyDetailState.postValue(ViewState.Error(it.message ?: "Unknown error occurred"))
                }
            } else {
                _storyDetailState.postValue(ViewState.Error("Session expired"))
            }
        }
    }

    override fun onError(throwable: Throwable) {
        super.onError(throwable)
        _storyDetailState.postValue(ViewState.Error(throwable.message ?: "Unknown error occurred"))
    }
}
