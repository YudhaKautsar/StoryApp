package com.yudhakautsar.storyapp.domain.usecase.story

import com.yudhakautsar.storyapp.domain.model.Story
import com.yudhakautsar.storyapp.domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow

class GetStoriesUseCase(private val repository: StoryRepository) {
    operator fun invoke(token: String): Flow<List<Story>> {
        return repository.getStories(token)
    }
}
