package com.yudhakautsar.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yudhakautsar.storyapp.base.BaseViewModel
import com.yudhakautsar.storyapp.base.ViewState
import com.yudhakautsar.storyapp.data.local.UserPreference
import com.yudhakautsar.storyapp.domain.model.Story
import com.yudhakautsar.storyapp.domain.usecase.story.GetStoriesUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class StoryListViewModel(
    private val getStoriesUseCase: GetStoriesUseCase,
    private val userPreference: UserPreference
) : BaseViewModel() {

    private val _storiesState = MutableLiveData<ViewState<List<Story>>>()
    val storiesState: LiveData<ViewState<List<Story>>> = _storiesState

    fun getStories() {
        _storiesState.value = ViewState.Loading
        launchWithExceptionHandler {
            val token = userPreference.getToken().first()
            if (token.isNotEmpty()) {
                getStoriesUseCase(token).collectLatest { stories ->
                    if (stories.isEmpty()) {
                        _storiesState.postValue(ViewState.Empty)
                    } else {
                        _storiesState.postValue(ViewState.Success(stories))
                    }
                }
            } else {
                _storiesState.postValue(ViewState.Error("Session expired"))
            }
        }
    }

    override fun onError(throwable: Throwable) {
        super.onError(throwable)
        _storiesState.postValue(ViewState.Error(throwable.message ?: "Unknown Error"))
    }

    fun logout() {
        viewModelScope.launch {
            userPreference.clearToken()
        }
    }
}
