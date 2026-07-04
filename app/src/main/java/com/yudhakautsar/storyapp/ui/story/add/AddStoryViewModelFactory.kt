package com.yudhakautsar.storyapp.ui.story.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yudhakautsar.storyapp.data.local.UserPreference
import com.yudhakautsar.storyapp.domain.usecase.story.AddStoryUseCase

class AddStoryViewModelFactory(
    private val addStoryUseCase: AddStoryUseCase,
    private val userPreference: UserPreference
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(addStoryUseCase, userPreference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
