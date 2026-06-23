package com.yudhakautsar.storyapp.ui.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yudhakautsar.storyapp.data.local.UserPreference
import com.yudhakautsar.storyapp.domain.usecase.story.GetStoriesUseCase

class StoryListViewModelFactory(
    private val getStoriesUseCase: GetStoriesUseCase,
    private val userPreference: UserPreference
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryListViewModel::class.java)) {
            return StoryListViewModel(getStoriesUseCase, userPreference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
