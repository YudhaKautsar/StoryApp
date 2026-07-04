package com.yudhakautsar.storyapp.ui.story.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yudhakautsar.storyapp.data.local.UserPreference
import com.yudhakautsar.storyapp.domain.usecase.story.GetStoryDetailUseCase

class StoryDetailViewModelFactory(
    private val getStoryDetailUseCase: GetStoryDetailUseCase,
    private val userPreference: UserPreference
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryDetailViewModel::class.java)) {
            return StoryDetailViewModel(getStoryDetailUseCase, userPreference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
